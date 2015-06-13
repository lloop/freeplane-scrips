// @ExecutionModes({ON_SELECTED_NODE, ON_SELECTED_NODE_RECURSIVELY})

	import javax.swing.*;
 
	// For establishing the new added attribute name
	def showDialog(String text) {
	    def dialog = new JDialog(ui.frame)
	    dialog.setSize(350, 450)
	    dialog.setLocationRelativeTo(ui.frame)
	    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
	    dialog.add(new JScrollPane(new JTextArea(text)))
	    ui.addEscapeActionToDialog(dialog)
	    dialog.setVisible(true)
	}
	
	def styleName = "Species"
	
	def attrName = "New Attribute"
	
	// Maybe set it up so that you can select which style conditional to add to

	// Get all the nodes that have the species style
	// TODO Right now it does all nodes. Make it selectable option for selected or selected recursive
	// Iterate and add the attribute 
	c.findAll().each {
	 if (it.hasStyle(styleName)) {
		it.attributes.add([attrName, ''])
	 }
	}
	

	




/*	// Existing attribs
	attribExists = node.attributes.map
	
	// Add the existing to the resulting
	attribAdds.each { k, v ->
		if (!attribExists.containsKey(k)) {
	    	node.attributes.add([k, attribAdds.get(k)])
		}
	}
	
	node.style.name = 'Species'*/