package org.sakaiproject.site.tool.helper.managegroupsectionrole.rsf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.site.tool.helper.managegroupsectionrole.impl.SiteManageGroupSectionRoleHandler;
import org.sakaiproject.site.util.SiteConstants;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;

import uk.ac.cam.caret.sakai.rsf.producers.FrameAdjustingProducer;
import uk.ac.cam.caret.sakai.rsf.util.SakaiURLUtil;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UILabelTargetDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.RawViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import uk.org.ponder.stringutil.StringList;

/**
 * 
 * @author
 *
 */
public class GroupListProducer 
        implements ViewComponentProducer, ActionResultInterceptor, DefaultView {
    
	/** Our log (commons). */
	private static Log M_log = LogFactory.getLog(GroupListProducer.class);
	
    public static final String VIEW_ID = "GroupList";
    public Map siteGroups;
    public SiteManageGroupSectionRoleHandler handler;
    public MessageLocator messageLocator;
    public SessionManager sessionManager;
    public FrameAdjustingProducer frameAdjustingProducer;
    
    public String getViewID() {
        return VIEW_ID;
    }
    
	public UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService userDirectoryService)
	{
		this.userDirectoryService = userDirectoryService;
	}
	
	private AuthzGroupService authzGroupService;
	public void setAuthzGroupService(AuthzGroupService authzGroupService)
	{
		this.authzGroupService = authzGroupService;
	}
	
    private TargettedMessageList tml;
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}
	
    public void fillComponents(UIContainer tofill, ViewParameters viewparams,
            ComponentChecker checker) {
    	
    	UIBranchContainer actions = UIBranchContainer.make(tofill,"actions:",Integer.toString(0));
    	UIInternalLink.make(actions,"add",UIMessage.make("group.newgroup"), new GroupEditViewParameters(GroupEditProducer.VIEW_ID, null));
    	UIInternalLink.make(actions,"auto_add",UIMessage.make("group.auto.newgroup"), new GroupAutoCreateViewParameters(GroupAutoCreateProducer.VIEW_ID, null));
    	
		UIOutput.make(tofill, "group-list-title", messageLocator.getMessage("group.list"));
		
		UIForm deleteForm = UIForm.make(tofill, "delete-group-form");
		
		List<Group> groups = null;
		groups = handler.getGroups();
      
		if (groups != null && groups.size() > 0)
        {
			StringList deletable = new StringList();
			M_log.debug(this + "fillComponents: got a list of " + groups.size() + " groups");
			
			// Create a multiple selection control for the tasks to be deleted.
			// We will fill in the options at the loop end once we have collected them.
			UISelect deleteselect = UISelect.makeMultiple(deleteForm, "delete-group",
					null, "#{SiteManageGroupSectionRoleHandler.deleteGroupIds}", new String[] {});

			//get the headers for the table
			UIMessage.make(deleteForm, "group-title-title","group.title");
			UIMessage.make(deleteForm, "group-size-title", "group.number");
			UIMessage.make(deleteForm, "group-remove-title", "editgroup.remove");
			  
			for (Iterator<Group> it=groups.iterator(); it.hasNext(); ) {
            	Group group = it.next();
            	String groupId = group.getId();
                UIBranchContainer grouprow = UIBranchContainer.make(deleteForm, "group-row:", group.getId());
                
                UIOutput.make(grouprow, "group-title-label", group.getTitle());
                UIInput name = 
                    UIInput.make(grouprow, "group-name-input", "#{SitegroupEditHandler.nil}", group.getTitle());
                UIOutput nameLabel = 
                    UIOutput.make(grouprow, "group-name-label", messageLocator.getMessage("group.title"));
                
                nameLabel.decorate(new UILabelTargetDecorator(name));
    			UIOutput.make(grouprow,"group-title",group.getTitle());
    			int size = 0;
    			try
    			{
    				AuthzGroup g = authzGroupService.getAuthzGroup(group.getReference()); 
    				Collection<Member> gMembers = g != null ? g.getMembers():new Vector<Member>();
    				size = gMembers.size();
    				if (size > 0)
    				{
	    				for (Iterator<Member> gItr=gMembers.iterator(); gItr.hasNext();){
	    		        	Member p = (Member) gItr.next();
	    		        	
	    		        	// exclude those user with provided roles and rosters
	    		        	String userId = p.getUserId();
		    				try
		    				{
		    					User u = userDirectoryService.getUser(userId);
		    				}
		    	        	catch (Exception e)
		    	        	{
		    	        		M_log.warn(this + "fillInComponent: cannot find user with id " + userId);
		    	        		// need to remove the group member
		    	        		size--;
		    	        	}
	    				}
    				}
    			}
    			catch (GroupNotDefinedException e)
    			{
    				M_log.debug(this + "fillComponent: cannot find group " + group.getReference());
    			}
    			UIOutput.make(grouprow,"group-size",String.valueOf(size));

    			UIInternalLink editLink = UIInternalLink.make(grouprow,"group-revise",messageLocator.getMessage("editgroup.revise"),  
    						new GroupEditViewParameters(GroupEditProducer.VIEW_ID, groupId));
    			editLink.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("editgroup.revise")+ ":" + group.getTitle()));
    			deletable.add(group.getId());
				UISelectChoice delete =  UISelectChoice.make(grouprow, "group-select", deleteselect.getFullID(), (deletable.size()-1));
				delete.decorators = new DecoratorList(new UITooltipDecorator(UIMessage.make("delete_group_tooltip", new String[] {group.getTitle()})));
				UIMessage message = UIMessage.make(grouprow,"delete-label","delete_group_tooltip", new String[] {group.getTitle()});
				UILabelTargetDecorator.targetLabel(message,delete);
            }
			
			deleteselect.optionlist.setValue(deletable.toStringArray());
			UICommand.make(deleteForm, "delete-groups",  UIMessage.make("editgroup.removechecked"), "#{SiteManageGroupSectionRoleHandler.processConfirmGroupDelete}");
			UICommand.make(deleteForm, "cancel", UIMessage.make("editgroup.cancel"), "#{SiteManageGroupSectionRoleHandler.processCancel}");
			
		}
		else
		{
			UIMessage.make(deleteForm, "no-group","group.nogroup");
			UICommand.make(deleteForm, "cancel", UIMessage.make("editgroup.cancel"), "#{SiteManageGroupSectionRoleHandler.processCancel}");
		}

		
		//process any messages
		tml = handler.messages;
        if (tml.size() > 0) {
			for (int i = 0; i < tml.size(); i ++ ) {
				UIBranchContainer errorRow = UIBranchContainer.make(tofill,"error-row:", Integer.valueOf(i).toString());
				String outString = "";
 				if (tml.messageAt(i).args != null ) {
 					outString = messageLocator.getMessage(tml.messageAt(i).acquireMessageCode(),tml.messageAt(i).args[0]);
 				} else {
 					outString = messageLocator.getMessage(tml.messageAt(i).acquireMessageCode());
 				}
 				UIMessage.make(errorRow,"error",outString);
			}
        }
    }
    
    // new hotness
    public void interceptActionResult(ARIResult result, ViewParameters incoming, Object actionReturn) {
        if ("confirm".equals(actionReturn)) {
            result.resultingView = new SimpleViewParameters(GroupDelProducer.VIEW_ID);
        } else if ("done".equals(actionReturn)) {
            Tool tool = handler.getCurrentTool();
            result.resultingView = new RawViewParameters(SakaiURLUtil.getHelperDoneURL(tool, sessionManager));
        }
    }
}
