package dustgrain

import dustgrain.cli.DustGrain
import dustgrain.cli.commands.data.Data
import dustgrain.cli.commands.data.Move
import dustgrain.cli.commands.data.System
import dustgrain.cli.commands.find.Find
import dustgrain.cli.commands.list.*
import dustgrain.cli.commands.list.List
import dustgrain.cli.commands.wikitable.*
import com.github.ajalt.clikt.core.subcommands
import dustgrain.cli.commands.find.Cell as FindCell
import dustgrain.cli.commands.find.Moves as FindMoves
import dustgrain.cli.commands.wikitable.Cell as WikitableCell

/**
 * Delegates execution to the main command for CLI handling.
 * @see DustGrain
 * @see List
 * @see Data
 */
fun main(args: Array<String>) =
    DustGrain().subcommands(
        List().subcommands(Moves(), Stat(), Stats(), Tables()),
        Data().subcommands(Move(), System()),
        Wikitable().subcommands(Headers(), Row(), Column(), WikitableCell()),
        Find().subcommands(FindCell(), FindMoves())
    ).main(args)
