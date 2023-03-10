package io.github.koba.ping

import io.github.monun.kommand.PluginKommand
import io.github.monun.kommand.getValue
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.Plugin

object KommandPing {
    private fun getInstance(): Plugin {
        return PingPlugin.instance
    }

    private lateinit var plugin: PingPlugin

    internal fun register(plugin: PingPlugin, kommand: PluginKommand) {
        KommandPing.plugin = plugin

        kommand.register("ping") {

            then("toggle") {
                requires { isOp }
                executes {
                    ping = if (ping) {
                        Bukkit.broadcastMessage("${ChatColor.YELLOW}독일핑이 비활성화 되었습니다.")
                        false
                    } else {
                        Bukkit.broadcastMessage("${ChatColor.YELLOW}독일핑이 활성화 되었습니다.")
                        true
                    }
                }
            }

            then("set") {
                then("pingDouble" to double()) {
                    requires { isOp }
                    executes {
                        val pingDouble: Double by it

                        if (pingDouble < 0) {
                            sender.sendMessage("${ChatColor.RED}0보다 작은 수는 입력할 수 없습니다.")
                        } else {
                            pingspeed = pingDouble

                            getInstance().config.set("ping", pingspeed)
                            getInstance().saveConfig()
                            Bukkit.broadcastMessage(String.format("${ChatColor.GREEN}지연 시간을 " + pingspeed + "ms로 설정했습니다."))
                        }
                    }
                }
            }

            then("check") {
                executes {
                    pingspeed = getInstance().config.getDouble("ping")

                    sender.sendMessage("${ChatColor.BOLD}----------독일핑----------")

                    if (ping) sender.sendMessage("${ChatColor.BOLD}독일핑 : ${ChatColor.GREEN}켜짐")
                    if (!ping) sender.sendMessage("${ChatColor.BOLD}독일핑 : ${ChatColor.RED}꺼짐")

                    if (pingspeed < 200) sender.sendMessage("${ChatColor.BOLD}지연시간 : ${ChatColor.GREEN}${pingspeed}ms")
                    if (pingspeed >= 200 && pingspeed < 300) sender.sendMessage("${ChatColor.BOLD}지연시간 : ${ChatColor.YELLOW}${pingspeed}ms")
                    if (pingspeed >= 300) sender.sendMessage("${ChatColor.BOLD}지연시간 : ${ChatColor.RED}${pingspeed}ms")
                }
            }
        }
    }
}
