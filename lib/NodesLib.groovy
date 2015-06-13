import org.freeplane.plugin.script.proxy.ScriptUtils
/*import org.freeplane.view.swing.map.attribute.AttributeView
import org.freeplane.view.swing.map.NodeView
import org.freeplane.features.map.NodeModel*/


/*
 *
*/
def static addSubNode(nodeText, styleName) {
	
	def node = ScriptUtils.node()
	
	// If child node doesn't already exist
	def compatChildren = node.find{it.text == nodeText}
	if (!compatChildren.size()) {
		// Create the nodes
		def newNode = node.createChild(nodeText)
		newNode.style.name = styleName
	}
	
}
