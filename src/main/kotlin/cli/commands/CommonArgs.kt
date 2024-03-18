package cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file

abstract class CommonArgs(helpText: String = "Common Arguments:") : CliktCommand(help = helpText) {
    val wiki by argument(help = "Dustloop sub-wiki")
    val character by argument(help = "Character's name as observed in the url")
    val pretty by option("-p", "--pretty", help = "Human readable text.").flag()
    val out by option("-o", "--out", help = "output file").file(mustBeWritable = true)
}