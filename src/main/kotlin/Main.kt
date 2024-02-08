import cli.DustGrain
import cli.commands.Data
import cli.commands.List
import com.github.ajalt.clikt.core.subcommands


fun main(args: Array<String>) = DustGrain().subcommands(List(), Data()).main(args)
