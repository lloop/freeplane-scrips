
// @ExecutionModes({ON_SINGLE_NODE})

// import org.freeplane.plugin.script.proxy.Proxy.Attributes


// def root = node.map.getRootNode() 

// def atts = node.attributes.getNames()
def atts = c.attributes.getNames()

node.text = atts

// Get all the attributes

// Iterate over attributes

// Test for previous existence of attribute name

// If name doesn't exist create a new node on the first level

// iterate the attribute values

// Test each value for previous existence

// If value doesn't exist then create a new node on the second level