
import org.freeplane.plugin.script.proxy.ScriptUtils
import org.freeplane.view.swing.map.attribute.AttributeView
import org.freeplane.view.swing.map.NodeView
import org.freeplane.features.map.NodeModel


/*
 *
*/
def static addNewAtts(attribAdds) {
	
	def node = ScriptUtils.node()
	def nodeAttributes = node.attributes
	
	// Add the existing to the new (if it doesn't already exist)
	attribAdds.each { k, v ->
		if (!nodeAttributes.map.containsKey(k)) {
	    	nodeAttributes.add([k, attribAdds.get(k)])
		}
	}
	
}


/*
 * Adds attributes to the node without checking if the att exists already
 * For the creation of new nodes that need atts with values
*/
def static addAttsVals(attribAdds) {
	
	def node = ScriptUtils.node()
	def nodeAttributes = node.attributes
	
	// Add the existing to the new
	attribAdds.each { k, v ->
	    	nodeAttributes.add([k, attribAdds.get(k)])
	}
	
}


/*
 *
*/
def static sortAttsTable() {
	
	def node = ScriptUtils.node()
	def nodeAttributes = node.attributes
	
	// Save original attributes
	def attribs = []
	nodeAttributes.names.eachWithIndex { name, i ->
	    attribs.add([name, nodeAttributes.get(i)])
	}
	
	// Define the comparator
	def caseInsensitive = true
	def comparator = {
	    def string = it[0]
	    if (caseInsensitive)
	        string.toLowerCase()
	    else
	        string
	}

	// Replace attributes with sorted
	nodeAttributes.clear()
	attribs.sort(comparator).each { k, v ->
	    nodeAttributes.add(k, v)
	}
	
}


/*
 * Todo this only works when the atts table is visible
 * So use AttributeView.areAttributesVisible()
 * and develop another way for whne the node is closed for recursives
*/
def static setTableOptWidth(node = ScriptUtils.node()) {
	
	// TODO The optimal width is only applied if the node is unfolded(has a viewer)
	
	// Set the attribute table to optimal width
	// Look for NodeView.getAttributeView()
	def NodeModel nModel = node.getDelegate()
	def AttributeView attrView = nModel.getViewers().find() {it instanceof NodeView}?.getAttributeView() 
	if(attrView) {
		attrView.setOptimalColumnWidths()
	}
	
}


/*
 * Todo this only works when the atts table is visible
 * So use AttributeView.areAttributesVisible()
 * and develop another way for whne the node is closed for recursives
*/
def static setTableConfigWidth(nameWidth, valueWidth, node = ScriptUtils.node()) {
	
	// Set very wide width 
	// NAME_WIDTH="100" VALUE_WIDTH="250"
	// TODO This is only applied if the node is unfolded(has a viewer)

	// Look for NodeView.getAttributeView()
	def NodeModel nModel = node.getDelegate()
	def AttributeView attrView = nModel.getViewers().find() {it instanceof NodeView}?.getAttributeView() 
	
	if(attrView) {
		// What we want is AttributeTableLayoutModel.setColumnWidth(final int col, final int width) 
		def attTableModel = attrView.getCurrentAttributeTableModel()
		// AttributeTableLayoutModel.setColumnWidth(final int col, final int width)
		attTableModel.setColumnWidth(0, nameWidth) 
		attTableModel.setColumnWidth(1, valueWidth) 
	}
	
}


/*
 *
*/
def static removeDupAtts() {
	
	def node = ScriptUtils.node()
	def nodeAttributes = node.attributes
	
	// Save original attributes
	def attribs = []
	nodeAttributes.names.eachWithIndex { name, i ->
	    attribs.add([name, nodeAttributes.get(i)])
	}

	// Clear the nodes attributes
	nodeAttributes.clear()

	// If the name value already exists don't add it back
	attribs.each{ k, v ->
	
		// If the key : value are not already in nodeAttributes add it back
		def inAlready = nodeAttributes.any{ entry -> entry.key == k && entry.value == v };
		if ( !inAlready ) {
			nodeAttributes.add(k, v)
		}
	
	}
	
}


/*
 *
*/
def static newDupAttRow() {

	def node = ScriptUtils.node()
	def nodeAttributes = node.attributes

	// If an attribute row is selected
	
	// Get the name of the selected row

	// Add a new row
/*	def randomAtt = */
	ScriptUtils.add()
	// AttributeView.addRow()
	// attributeTable.insertRow(attributeTable.getRowCount());
	// add(String, Object) - Method in interface org.freeplane.plugin.script.proxy.Proxy.Attributes
	
	// If no att row is selcted add an empty one
	
}