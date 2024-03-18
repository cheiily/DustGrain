package cli.commands.list

import cli.commands.CommonArgs
import com.github.ajalt.clikt.core.NoOpCliktCommand
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model

/**
 * Command responsible for handling list requests.
 */
class List : NoOpCliktCommand("Grouping command for plain-listing data from the wiki. See subcommands for details.")
//TODO package the entire thing into DustGrain, try redoing the list pull to categorize moves