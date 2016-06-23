package com.google.simple.teampicker;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

public class OfyHelper implements ServletContextListener {
  public void contextInitialized(ServletContextEvent event) {
    ObjectifyService.register(Game.class);
    ObjectifyService.register(Season.class);
    ObjectifyService.register(Team.class);
  }

  public void contextDestroyed(ServletContextEvent event) {
    // Nothing to do.
  }
}
