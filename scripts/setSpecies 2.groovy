
// @ExecutionModes({ON_ALL_SELECTED_NODES})

	// New attribs
	def attribAdds = [
						"Application": '', 
						"Application - Medicinal": '',
						"Application - Edible": '', 
						"Application - Fermentation": '',
						"Characteristic": '', 
						"Characteristic - Resistance": '',
						"Characteristic - Sensitivity": '',
						"Characteristic - Growth Habit": '', 
					    "Characteristic - Leaf Type": '',
						"Characteristic - Toxicity": '',
					    "Conditions - Light": '', 
						"Conditions - Soil Characteristics": '', 
						"Conditions - Soil Moisture": '', 
						"Conditions - Soil PH": '', 
						"Conditions - Soil Texture": '',
						"Dimensions - Height Max": '', 
						"Dimensions - Height Min": '',
						"Dimensions - Spacing": '', 
						"Dimensions - Root Depth": '',
						"Compatibility - Guild": '',
						"Schedule - Bearing Age": '',
						"Schedule - Duration": '',
						"Schedule - Flower Period": '',
						"Schedule - Harvest Period": '',
						"Schedule - Lifespan": '',
						"Schedule - Seed Germination": '',  
						"Sexuality - Behavior": '', 
						"Sexuality - Flower Tyoe": '', 
						"Sexuality - Pollination": ''
						]


	// Existing attribs
	// Need to create map for the containsKey method
	def attribExists = node.attributes.map
	def nodeAttributes = node.attributes
	
	// Add the existing to the resulting
	attribAdds.each { k, v ->
		if (!attribExists.containsKey(k)) {
	    	nodeAttributes.add([k, attribAdds.get(k)])
		}
	}
	
	// Sort the attributes
	def caseInsensitive = true
	def comparator = {
	    def string = it[0]
	    if (caseInsensitive)
	        string.toLowerCase()
	    else
	        string
	}

	// Save original attributes
	def attribs = []
	nodeAttributes.names.eachWithIndex { name, i ->
	    attribs.add([name, nodeAttributes.get(i)])
	}

	// Replace attributes with sorted
	nodeAttributes.clear()
	attribs.sort(comparator).each { k, v ->
	    nodeAttributes.add(k, v)
	}
	
	
	// http://freeplane.sourceforge.net/doc/api/
	// for dialogs and such when this is an add-on
	/*	ui.informationMessage("Hello World!")*/
	
	// If 'compatible plantings' child nodes don't already exist
	def compatChildren = node.find{it.style.name == 'Compatible'}
	if (!compatChildren.size()) {
		// Create the nodes
		def compatable = node.createChild("Compatible Plantings")
		def incompatible = node.createChild("Incompatible Plantings")
		incompatible.style.name = 'Compatible'
		compatable.style.name = 'Compatible'
	}
	
	node.style.name = 'Species'
	