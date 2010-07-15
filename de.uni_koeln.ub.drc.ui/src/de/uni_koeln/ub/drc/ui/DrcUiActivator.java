/**************************************************************************************************
 * Copyright (c) 2010 Fabian Steeg. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation
 *************************************************************************************************/
package de.uni_koeln.ub.drc.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.security.auth.login.LoginException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.osgi.framework.BundleContext;

import de.uni_koeln.ub.drc.data.User;

/**
 * Bundle activator.
 * @author Fabian Steeg (fsteeg)
 */
public final class DrcUiActivator extends Plugin {

  public static final String PLUGIN_ID = "de.uni_koeln.ub.drc.ui";

  private static final String JAAS_CONFIG_FILE = "jaas_config";

  private static DrcUiActivator instance;
  private ILoginContext loginContext;

  @Override
  public void start(final BundleContext context) throws Exception {
    super.start(context);
    instance = this;
    login(context);
  }

  public String usersFolder() {
    return fileFromBundle("users").getAbsolutePath();
  }

  public User currentUser() {
    User user = null;
    try {
      user = (User) getLoginContext().getSubject().getPrivateCredentials().iterator().next();
    } catch (LoginException e) {
      e.printStackTrace();
    }
    return user;
  }

  private void login(final BundleContext context) throws Exception {
    String configName = "SIMPLE";
    URL configUrl = context.getBundle().getEntry(JAAS_CONFIG_FILE);
    loginContext = LoginContextFactory.createContext(configName, configUrl);
    try {
      loginContext.login();
    } catch (LoginException e) {
      IStatus status = new Status(IStatus.ERROR, "de.uni_koeln.ub.drc.ui", "Login failed", e);
      int result = ErrorDialog.openError(null, "Error", "Login failed", status);
      if (result == ErrorDialog.CANCEL) {
        stop(context);
        System.exit(0);
      } else {
        login(context);
      }
    }
  }

  /**
   * @return The active bundle, or null
   */
  public static DrcUiActivator instance() {
    return instance;
  }

  /**
   * @return The context for the logged in user.
   */
  public ILoginContext getLoginContext() {
    return loginContext;
  }

  @Override
  public void stop(final BundleContext context) throws Exception {
    instance = null;
    super.stop(context);
  }

  /**
   * @param location The name of the file to retrieve, relative to the bundle root
   * @return The file at the given location
   */
  public File fileFromBundle(final String location) {
    try {
      URL resource = getBundle().getResource(location);
      if (resource == null) {
        System.err.println("Could not resolve: " + location);
        return null;
      }
      return new File(FileLocator.resolve(resource).toURI());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
