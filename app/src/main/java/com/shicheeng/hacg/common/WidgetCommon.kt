package com.shicheeng.hacg.common


//从 https://github.com/yueeng/hacg/blob/master/app/src/main/java/io/github/yueeng/hacg/Common.kt 复制而来
private val rmagnet = """\b([a-zA-Z0-9]{32}|[a-zA-Z0-9]{40})\b""".toRegex()
private val rbaidu = """\b([a-zA-Z0-9]{8})\b\s+\b([a-zA-Z0-9]{4})\b""".toRegex()
fun String.magnet(): Sequence<String> = rmagnet.findAll(this).map { it.value } +
        rbaidu.findAll(this).map { m -> "${m.groups[1]!!.value},${m.groups[2]!!.value}" }

