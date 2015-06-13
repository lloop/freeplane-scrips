// @ExecutionModes({on_single_node="main_menu_scripting/freeplaneGTD[addons.parseShorthand]"})
//=========================================================
// Freeplane GTD+
//
// Copyright (c)2014 Gergely Papp
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//=========================================================
package freeplaneGTD

//=========================================================
// references
//=========================================================
import org.freeplane.core.util.TextUtils
import org.freeplane.plugin.script.proxy.Proxy

//=========================================================
// classes
//=========================================================

//---------------------------------------------------------
// GTDMapReader: reads and parses GTD map for next actions
//---------------------------------------------------------
class GTDMapReader {
    private static GTDMapReader instance = new GTDMapReader()

    public String iconNextAction
    public String iconProject
    public String iconToday
    public String iconDone
    public Map contextIcons

    // RP Mod
    // this ia a variable outside the recursive function findNextActions to
    // be able to only display the leaf nodes on a branch that need to be completed
    private List nodeCache

    static GTDMapReader getInstance() {
        return instance
    }

    private GTDMapReader() {
    }

    //--------------------------------------------------------------
    // Convert next action shorthand notation with recursive walk:
    // shorthand: *<next action> @<Context> [<who>] {<when>} #<priority>
    // becomes:
    // node.text     = <next action>
    // node['Where'] = <where>
    // node['Who']   = <who>
    // node['When']  = <when>
    // node['Priority']  = <priority>
    //
    public void convertShorthand(Proxy.Node rootNode) {
        findIcons(rootNode)
        internalConvertShorthand(rootNode)
    }


    //--------------------------------------------------------------
    //Get icon key names from Settings/Icons nodes
    void findIcons(Proxy.Node thisNode) {
        // Get icon keys for next actions and projects
        iconNextAction = "yes"
        iconProject = "list"
        iconToday = "bookmark"
        iconDone = "button_ok"
        contextIcons = [:]

        internalFindIcons(thisNode)
    }

    public List getActionList(Proxy.Node rootNode, boolean filterDone) {
        return findNextActions(rootNode, filterDone, iconProject, iconNextAction, iconToday, iconDone)
    }


    //Get icon key names from Settings/Icons nodes
    private void internalFindIcons(Proxy.Node thisNode) {
        String firstIcon = thisNode.icons.first;
        String nodeText = thisNode.text.trim();

        if (nodeText =~ '^Icon:') {
			if (firstIcon ==~ /^full\-\d$/) {
                    throw new Exception('Trying to reuse priority icon:' + firstIcon + ' on node ' + nodeText)
			}
            if (nodeText == 'Icon: Next action') {
                if (['help', iconProject, iconToday, iconDone].contains(firstIcon)
                        || contextIcons.values().contains(firstIcon)) {
                    throw new Exception('Trying to reuse icon:' + firstIcon + ' on node ' + nodeText)
                }
                iconNextAction = firstIcon
            } else if (nodeText == 'Icon: Project') {
                if (['help', iconNextAction, iconToday, iconDone].contains(firstIcon)
                        || contextIcons.values().contains(firstIcon)) {
                    throw new Exception('Trying to reuse icon:' + firstIcon + ' on node ' + nodeText)
                }
                iconProject = firstIcon
            } else if (nodeText == 'Icon: Today') {
                if (['help', iconNextAction, iconProject, iconDone].contains(firstIcon)
                        || contextIcons.values().contains(firstIcon)) {
                    throw new Exception('Trying to reuse icon:' + firstIcon + ' on node ' + nodeText)
                }
                iconToday = firstIcon
            } else if (nodeText == 'Icon: Done') {
                if (['help', iconNextAction, iconProject, iconToday].contains(firstIcon)
                        || contextIcons.values().contains(firstIcon)) {
                    throw new Exception('Trying to reuse icon:' + firstIcon + ' on node ' + nodeText)
                }
                iconDone = firstIcon
            } else if (nodeText =~ '^Icon: @') {
                String context = nodeText.replaceAll('^Icon: @([^\\s]*)', '$1');
                if (['help', iconNextAction, iconProject, iconToday, iconDone].contains(firstIcon)
                        || contextIcons.values().contains(firstIcon)) {
                    throw new Exception('Trying to reuse icon:' + firstIcon + ' on node ' + nodeText)
                }
                contextIcons[context] = firstIcon
            }
        }

        thisNode.children.each {
            internalFindIcons(it);
        }
    }

    //--------------------------------------------------------------
    // Convert next action shorthand notation with recursive walk:
    // shorthand: *<next action> @<Context> [<who>] {<when>} #<priority>
    // becomes:
    // node.text     = <next action>
    // node['Where'] = <where>
    // node['Who']   = <who>
    // node['When']  = <when>
    // node['Priority']  = <priority>
    //
    public void internalConvertShorthand(Proxy.Node thisNode) {
        String nodeText = thisNode.text.trim();

        if (nodeText.length() > 0 && nodeText.charAt(0) == '?') {
            nodeText = nodeText.substring(1).trim();
            thisNode.text = nodeText;
            thisNode.icons.add('help');
        }
        if ((nodeText.length() > 0 && nodeText.charAt(0) == '*') || (thisNode.icons.icons.contains(iconNextAction))) {
            Map fields = parseShorthand(nodeText);
            thisNode.text = fields['action'];

            def nodeAttr = [:] as Map<String, Object>
            thisNode.attributes.names.eachWithIndex { name, i -> nodeAttr[name] = thisNode.attributes.get(i) }

            if (fields['context']) nodeAttr['Where'] = fields['context']
            if (fields['delegate']) nodeAttr['Who'] = fields['delegate']
            if (fields['when']) nodeAttr['When'] = fields['when']
            if (fields['priority']) nodeAttr['Priority'] = fields['priority']

            List contexts = nodeAttr['Where'] ? nodeAttr['Where'].split(',') : []
            contextIcons.each {
                context, icon ->
                    if (thisNode.icons.icons.contains(icon)) {
                        contexts << context
                    }
            }
            if (contexts?.size()) {
                nodeAttr['Where'] = contexts.unique().join(',')
            }

            thisNode.icons.each {
				if (it ==~ /^full\-\d$/) {
					nodeAttr['Priority'] = it[5]
				}
			}

            thisNode.attributes = nodeAttr;
            if (!thisNode.icons.icons.contains(iconNextAction)) {
                thisNode.icons.add(iconNextAction);
            }

            contexts.each {
                if (contextIcons.keySet().contains(it)) {
                    String contextIcon = contextIcons[it]
                    if (!thisNode.icons.icons.contains(contextIcon)) {
                        thisNode.icons.add(contextIcon);
                    }
                }
            }


            thisNode.icons.each {
                if (it ==~ /^full\-\d$/) {
                    thisNode.icons.remove(it)
                }
            }
			if (nodeAttr['Priority']) {
				String priorityIcon = 'full-' + nodeAttr['Priority'];
                thisNode.icons.add(priorityIcon);
			}
        }

        thisNode.children.each {
            internalConvertShorthand(it);
        }
    }
    //--------------------------------------------------------------
    // find parent for the next action, either direct (task) or indirect (project)
    private static String findNextActionProject(Proxy.Node thisNode, String iconProject) {
        Proxy.Node parentNode = thisNode.getParent();
        String retval = ''
        if (parentNode != null && thisNode.isDescendantOf(parentNode)) {
            Proxy.Node walker = parentNode;
            while (walker) {
                if (walker.icons.contains(iconProject)) {
                    retval = walker.text + (retval ? '/' + retval : '');
                }
                walker = walker.parent;
            }
        }
        return retval ? retval : parentNode.text;
    }

    //--------------------------------------------------------------
    // recursive walk through nodes to find Next Actions
    // todo RP Add a feature that only includes the next action if it has no 'next action' children or all 'next action' children are done
    // todo RP Because the method is recursive will need to set up some kind of static variable outside the method to keep track of the whether the upper node is valid as 'next task'
    // todo Each active node (the one being processed by this method) is considered the most valid until a child is considered valid
    // todo RP I am looking to only show the leaf nodes of a branch that are next tasks
    private
    def findNextActions(Proxy.Node thisNode, boolean filterDone, String iconProject, String iconNextAction, String iconToday, String iconDone) {
        def icons = thisNode.icons.icons;
        def naAction = thisNode.text;
        def naNodeID = thisNode.id;

        // use index method to get attributes
        String naContext = thisNode['Where'].toString()
        String naWho = thisNode['Who'].toString()
        Object naWhen = thisNode['When']
		String naPriority = thisNode['Priority'].toString()

        // take care of missing attributes. null or empty string evaluates as boolean false
        if (!naWhen) {
            naWhen = TextUtils.getText("freeplaneGTD.view.when.this_week")
        } else {
            naWhen = DateUtil.normalizeDate(naWhen)
            thisNode['When'] = naWhen
        }

        def result = [];
        // include result if it has next action icon and its not the icon setting node for next actions
        if (icons.contains(iconNextAction)) {
            if (!(naAction =~ /Icon:/)) {
                def naProject = findNextActionProject(thisNode, iconProject);
                if (icons.contains(iconToday)) {
                    naWhen = TextUtils.getText('freeplaneGTD.view.when.today');
                }
                boolean done = icons.contains(iconDone)
                // todo RP if the node passes the 'not done' test and is a task then it should replace the 'outside variable' value
                // todo If it's not done and the filter is not done
                // todo Change this to replacing the value of outside variable with this
                if (!(filterDone && done)) {
                    nodeCache = [node    : thisNode,
                               action  : naAction,
                               project : naProject,
                               context : naContext,
                               who     : naWho,
                               when    : naWhen,
                               priority: naPriority,
                               nodeID  : naNodeID,
                               done    : done]
                }
//                if (!(filterDone && done)) {
//                    result << [node    : thisNode,
//                               action  : naAction,
//                               project : naProject,
//                               context : naContext,
//                               who     : naWho,
//                               when    : naWhen,
//                               priority: naPriority,
//                               nodeID  : naNodeID,
//                               done    : done]
//                }
            }
        }

        // todo Check the children for any tasks not done and if they exist replace the value of the outside variable with them,
        // todo if they don't exist assign the outside variable to results

        // todo RP Run this recursion only to reset or keep the outside variable
        thisNode.children.each {
            result.addAll(findNextActions(it, filterDone, iconProject, iconNextAction, iconToday, iconDone))
        }

        // todo RP After the recursion the outside variable should have all nodes that matter. Add it to result.
        // todo All the leaf nodes will be included because the variable is only cleared when children are valid

        result << nodeCache

        return result;
    }

    //--------------------------------------------------------------
    // Parse next action shorthand notation
    private static Map parseShorthand(String nodeText) {
        Map fields = [:] as Map<String, Object>

        String toParse = nodeText
        def delegates = []
        while (toParse.matches('^.*\\[([^\\]]*)\\].*$')) {
            delegates.addAll(toParse.replaceFirst('[^\\[]*\\[([^\\]]*)\\].*', '$1').split(',')*.trim())
            toParse = toParse.replaceFirst('\\s*\\[[^\\]]*\\]\\s*', ' ').trim()
        }
        if (delegates) {
            fields['delegate'] = delegates.unique().join(',')
        }
        if (toParse.indexOf('{') >= 0) {
            fields['when'] = DateUtil.normalizeDate(toParse.replaceAll('^.*\\{(.*)\\}.*$', '$1').trim())
            toParse = toParse.replaceAll('\\s*\\{.*\\}\\s*', ' ').trim()
        }
        if (toParse =~ /#\d/) {
            fields['priority'] = toParse.replaceAll('^.*#(\\d).*$', '$1').trim()
            toParse = toParse.replaceAll('#\\d', '').trim()
        }
        def contexts = []
        while (toParse =~ '^[^@]*@([^@\\s\\*]+).*') {
            contexts.addAll(toParse.replaceFirst('^[^@]*@([^@\\s\\*]+).*', '$1').split(',')*.trim())
            toParse = toParse.replaceFirst('\\s*@[^@\\s\\*]+\\s*', ' ').trim()
        }
        if (contexts) {
            fields['context'] = contexts.unique().join(',')
        }
        fields['action'] = toParse.replaceAll('^\\s*\\*\\s*', '').trim()
        return fields;
    }
}
