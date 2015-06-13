import org.freeplane.plugin.script.proxy.ScriptUtils
import groovy.util.*
import groovy.xml.*



/*
 * Get configs (All node.text values below the node with this style and node.text)
 * style name, parent node text
*/
def static getConfigs(style, nodeText) {
	
	def c = ScriptUtils.c()
	
	HashMap<String, String> retConfigs = new HashMap<String, String>();
	def childConfigs = c.find{it.style.name == style && it.text == nodeText}.children
	
	// todo remove the double eaches
	childConfigs.each{ atts ->
		atts.each{
			retConfigs.put(it.text, new String(''))			
		}
	}
	
	return retConfigs
	
}


/*
 * Get config (the "Config" attribute value for the node 
 * with this node.text and this style)
 * style name, parent node text
*/
def static getConfig(style, nodeText) {
	
	def c = ScriptUtils.c()
	
/*	int configValue = c.find{it.style.name == style && it.text == nodeText}[0]["Config"].getNum0()*/
	
	// Try it with the details section so that the Attributes are not polluted
	def configNode = c.find{it.style.name == style && it.text == nodeText}[0]
	def configDetails = configNode.detailsText ?: "<body><p>125</p></body>"
	def parsedDetails = new XmlParser().parseText(configDetails)
	String configVal = parsedDetails.depthFirst().findAll { it.name() == 'p' }[0].text()
	
	
	return Integer.parseInt(configVal)
	
}
