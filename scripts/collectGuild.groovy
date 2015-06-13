// @ExecutionModes({ON_SELECTED_NODE})

import groovy.swing.SwingBuilder
import groovy.util.*
import groovy.xml.* 


// If the node has the style of "Guild Membership" and it's parent node has the style of "Guild"
if (node.hasStyle("Guild Membership") && node.parent.hasStyle("Guild")) {

	// Save the coretext value of the parent node that has the style of "Guild"
	// This will be the value in the attribute that finds the source nodes
	def guildName = node.parent.text

	// Get and delete all the child nodes
	def originalChildren = node.children
	originalChildren.each { 
		it.delete()
	}

	// Find all nodes (guildMemberSources) that have the attribute "Data - Guild" 
	// and the value of that attribute equals the value of the core text of the parent node that has the "Guild" style	
	// Returns a list of NodeProxy's
	List<Object> guildMemberSources = new ArrayList<Object>()
	
/*	logger.info(node.map.root.toString())
	logger.info(guildName)
	logger.info(guildMemberSources.toString())*/
	
	walkNodeChildren(node.map.root, guildName, guildMemberSources)
	
	if (guildMemberSources.isEmpty()) {
		
		errorPopup("No Member Nodes")
		
	} else {

		// For each guildMemberSources item. 
		guildMemberSources.each {
	
			// Save the node ID for a link back to the source node
			def nodeId = it.id
		
			// Calculate the source style name. If the source style name has 
			// parenthesis like (subtribe), remove that part
			def textInParens = ~/\(.*\)/
			def sourceStyleName = it.style.name - textInParens

			// For each guildMemberSources item. Create a child node of the node variable
			def guildChlldNode = node.createChild()
		
			// Get the P elements from the source node core text
			def sourceCoreText = htmlUtils.isHtmlNode(it.text) ? it.text : htmlUtils.plainToHTML(it.text)
			def coreLines = new XmlParser().parseText(sourceCoreText)
			def corePElements = coreLines.depthFirst().findAll{it.name() == 'p'}
		
			logger.info(corePElements.toString())
		
			// Get the P elements from the source node details
			def sourceDetails = it.detailsText ?: "<body></body>"
			def detailsLines = new XmlParser().parseText(sourceDetails)
			def detailsPElements = detailsLines.depthFirst().findAll { it.name() == 'p' }
		
			// Establish the MarkupBuilder
			def writer = new StringWriter() 
			def builder = new MarkupBuilder(writer)
			def fontSize = 2

			// Build the initial html markup
			builder.html {
				head(){}
				body(){

					// Add the p elements from the source node core
					corePElements.eachWithIndex{ sourceCore, index ->
						def sourceText = sourceCore.text()
						if (index == 0) {
							p {
								b (sourceText)
							}
						} else {
							// If the font element already exists within the p element
							// then the elem.text will return an empty string
							// because it is looking for text on the wrong level
							if (sourceText.length() == 0) {
								def subElem = sourceCore.depthFirst().findAll{it.text().length() > 0} 
								def subElemText = subElem[0].text()
								p {
									font (size: fontSize, subElemText) 
								}						
							} else {
								p {
									font (size: fontSize, sourceText) 
								}
							}						
						}	
					}
				
					// Add the p elements from the source node details
				    detailsPElements.each{sourceDetail->
						p {
							font (size: fontSize, sourceDetail.text()) 
						}
				    }
				}
			}

			// Assign the markup to the guildChlldNode.text
			guildChlldNode.text = writer.toString()
		
			// Set a link to the source node
			guildChlldNode.link.text = "#" + nodeId
		
			// For each item. Assign the "Guild Member " + source style name.
			guildChlldNode.style.name =  "Guild Member " + sourceStyleName

			// For each item. Create the attributes "Data - Guild Element" and "Data - Food Forest Layer"
			// and the value of the source node
			// Get the attributes that will be assigned to the guild member from the configs
			def memberAtts = ConfigsLib.getConfigs("Config", "Guild Member")
			// For each memberAtts item (hashmap). 
			if (memberAtts) {
				memberAtts.each { k, v ->
					// Get the corresponding value of the guildMemberSources source 
					// node att with the memberAtts title
					def guildMemberAttVal = it[k] ?: "Compatible_Plant"
			
					// Set an attribute on the guildChlldNode with the value matching 
					// the same att on the guildMember
					guildChlldNode.attributes.add(k, guildMemberAttVal)
				}
			}

			// Set attribute table to config wide
			// Get the config values for the column widths
			// style name, parent node text
			def nameColumnWidth = ConfigsLib.getConfig("Config", "Name Column Width")
			def valueColumnWidth = ConfigsLib.getConfig("Config", "Value Column Width")
			AttsLib.setTableConfigWidth(nameColumnWidth, valueColumnWidth, guildChlldNode)
	
		}

		// Sort the child nodes according to "Data - Guild Element" value
		// and set the widths to the maximum node width of the group
		// TODO - create folders for the guild postion values and put each new node into it's
		// correct one
		def sorted = new ArrayList(node.children).sort{ it["Data - Guild Element"] }
		sorted.eachWithIndex { it, i ->
		    it.moveTo(node, i)
		}
	}

} else {
	
	errorPopup("Invalid Guild Node")
	
}


/*
 * Recurse and process leaf nodes. 
 * Stop recursion if node has "Data - Guild" att woth guild name value
*/
def walkNodeChildren(n, guildName, nodeList) {
	// First thing check for guild membership and process
	if (n["Data - Guild"] && n["Data - Guild"] == guildName) {
		nodeList.add(n)
	} else {
		// If the node doesn't have the att then check for children
		if (n.children) {
			n.children.each { child ->
				walkNodeChildren(child, guildName, nodeList)
			}				
		}
	}
}


/*
 * Error popups for different situations
*/
def errorPopup(situation) {
	
	def popupText
	
	switch (situation) {
		case "Invalid Guild Node":
			popupText = "The selected node is not valid for this script. Perhaps the styles are not correct for the parent node?"
			break
		case "No Member Nodes":
			popupText = "Couldn't find any members for this guild."
			break
		default:
			popupText = "General Error"
			break
	}
	
	// todo Learn more about SwingBuilder and develop this
	swing = new SwingBuilder();
	gui = swing.frame(title:"Invalid Node", size:[300,300]) {
	  label(text: popupText);
	}
	gui.show();
	
}
