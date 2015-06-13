
// @ExecutionModes({ON_ALL_SELECTED_NODES})

import groovy.util.*
import groovy.xml.*

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
	
	// Collect the attribute configs 
	// style name, parent node text, c
	def configAtts = ConfigsLib.getConfigs("Config", "Journal Entry")
	
	// Add the config atts to the existing
	if (configAtts) {
		AttsLib.addNewAtts(configAtts)	
	}
	
	// Sort atts and set width of table
	if (node.attributes) {
		
		// Sort attributes
		AttsLib.sortAttsTable()
		
		// Set attribute table to very wide
		// Get the config values for the column widths
		// style name, parent node text
		def nameColumnWidth = ConfigsLib.getConfig("Config", "Name Column Width")
		def valueColumnWidth = ConfigsLib.getConfig("Config", "Value Column Width")
		
/*		logger.info("nameColumnWidth: " + nameColumnWidth)*/
		
		AttsLib.setTableConfigWidth(nameColumnWidth, valueColumnWidth) 
		
	}
	
	// Set up the core text with todays date or the original date object/text
	CoreLib.setupJournalEntryCore()
	
	node.style.name = "Journal Entry"
	
