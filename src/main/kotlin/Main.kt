import cli.DustGrain
import cli.commands.Data
import cli.commands.List
import com.github.ajalt.clikt.core.subcommands

/**
 * Delegates execution to the main command for CLI handling.
 * @see DustGrain
 * @see List
 * @see Data
 */
fun main(args: Array<String>) = DustGrain().subcommands(List(), Data()).main(args)
