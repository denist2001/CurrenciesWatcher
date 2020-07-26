package com.codechallenge.revolutcodechallenge

import javax.annotation.Resource

data class FlagLink(val name: String, @Resource val link: Int, val description: String) {
}