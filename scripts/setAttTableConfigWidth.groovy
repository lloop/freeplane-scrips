// @ExecutionModes({ON_ALL_SELECTED_NODES})


import groovy.util.*
import groovy.xml.*

if (node.attributes) {

	// Set attribute table to very wide
	// Get the config values for the column widths
	// style name, parent node text, c
	def nameColumnWidth = ConfigsLib.getConfig("Config", "Name Column Width")
	def valueColumnWidth = ConfigsLib.getConfig("Config", "Value Column Width")

	AttsLib.setTableConfigWidth(nameColumnWidth, valueColumnWidth) 
	
}