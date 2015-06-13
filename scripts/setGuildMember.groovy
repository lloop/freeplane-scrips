
// @ExecutionModes({ON_ALL_SELECTED_NODES})

import groovy.util.*
import groovy.xml.*
	
	// Collect the attribute configs 
	def configAtts = ConfigsLib.getConfigs("Config", "Guild Member")
	
	// Add the config atts to the existing
	if (configAtts) {
		AttsLib.addNewAtts(configAtts)	
	}
	
	// Sort atts and set width of table
	if (node.attributes) {
		// Sort
		AttsLib.sortAttsTable()
		
		// Set attribute table to very wide
		// Get the config values for the column widths
		// style name, parent node text, c
		def nameColumnWidth = ConfigsLib.getConfig("Config", "Name Column Width")
		def valueColumnWidth = ConfigsLib.getConfig("Config", "Value Column Width")
		
		// Optimal width
		AttsLib.setTableConfigWidth(nameColumnWidth, valueColumnWidth)
	}
	
	// Add sub-nodes
/*	NodesLib.addSubNode("Compatible Plantings", "Compatible")
	NodesLib.addSubNode("Incompatible Plantings", "Compatible")*/
	
	// Collect the languages configs
/*	def langsConfigs = ConfigsLib.getConfigs("Config", "Languages")*/
	
	//
/*	DetailsLib.setupDetails(langsConfigs)*/
	
	// Set up the core text to have smaller font sizes for each lower p element
/*	CoreLib.setupCoreFontSizes(htmlUtils)*/
	
	node.style.name = "Guild Member"
	
	