
// @ExecutionModes({ON_ALL_SELECTED_NODES, ON_SELECTED_NODE_RECURSIVELY})
	
	
/*	AttsLib.newDupAttRow()*/
	
/*		def node = ScriptUtils.node()*/
		def nodeAttributes = node.attributes

		// If attributes exist
		if (nodeAttributes.count > 0) {
			
			logger.info("attributes exist")

			// If attributes are visible


			// If an attribute row is selected

			// Get the name of the selected row
			// java.lang.String nodeAttributes.getKey(int index)
			// returns the attribute key at the given index.

			// Add a new row
/*			def randomAtt = nodeAttributes.getNames()*/
		
/*			nodeAttributes.add(randomAtt[0], "")*/
			// AttributeView.addRow()
			// attributeTable.insertRow(attributeTable.getRowCount());
			// add(String, Object) - Method in interface org.freeplane.plugin.script.proxy.Proxy.Attributes

			// If no att row is selcted add an empty one
		
		} else {
			
			// AttributeView.getAttributeRegistry()
			
			// Create a new attribute
			def randomAtt = nodeAttributes.getNames()
			
/*			logger.info(randomAtt)*/
		
			nodeAttributes.add("experiment", "")
			
		}