package edu.brown.cs.user.CS32Final.Entities.Event;

import edu.brown.cs.user.CS32Final.Entities.Account.Account;

/**
 * Created by lc50 on 4/10/16.
 */
public class EventRequest extends Event {
    private Account userRequester;

    public EventRequest(Event event, Account user) {
        super(event.getId(), event.getState(), event.getName(), event.getDesc(), event.getImage(), event.getHost(),
                event.getMembers(), event.getMaxMembers(), event.getCostPP(), event.getLocation(), event.getTags());
        this.userRequester = user;
    }

    public Account getUser() {
        return userRequester;
    }

    public void setUser(Account user) {
        this.userRequester = user;
    }
}
