/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.env;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.nio.file.Path;

import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoPath;

public class GoEnvironmentTest extends CommonGoToolingTest {
	
	private static final Path WS_BAR = TESTS_WORKDIR.resolve("WorkspaceBar");
	private static final Path WS_FOO = TESTS_WORKDIR.resolve("WorkspaceFoo");
	
	@Test
	public void test_GoPath() throws Exception { test_GoPath$(); }
	public void test_GoPath$() throws Exception {
		GoPath goPath = new GoPath(WS_FOO + File.pathSeparator + WS_BAR);
		 
		assertAreEqual(goPath.findGoPathEntry(WS_FOO.resolve("xxx")), WS_FOO);
		assertAreEqual(goPath.findGoPathEntry(WS_BAR.resolve("xxx")), WS_BAR);
		assertAreEqual(goPath.findGoPathEntry(TESTS_WORKDIR.resolve("xxx")), null);
		
		assertAreEqual(goPath.findGoPackageForSourceModule(WS_FOO.resolve("xxx/m.go")), null);
		assertAreEqual(goPath.findGoPackageForSourceModule(WS_FOO.resolve("src/xxx/m.go")), path("xxx"));
		assertAreEqual(goPath.findGoPackageForSourceModule(WS_FOO.resolve("src/xxx/zzz/m.go")), path("xxx/zzz"));
		assertAreEqual(goPath.findGoPackageForSourceModule(WS_FOO.resolve("src/m.go")), null);
		assertAreEqual(goPath.findGoPackageForSourceModule(WS_BAR.resolve("src/xxx/m.go")), path("xxx"));
		assertAreEqual(goPath.findGoPackageForSourceModule(WS_BAR.resolve("src/src/src/m.go")), path("src/src"));
		assertAreEqual(goPath.findGoPackageForSourceModule(TESTS_WORKDIR.resolve("src/xxx/m.go")), null);
		
		// Test empty case
		goPath = new GoPath("");
		assertTrue(goPath.isEmpty());
		assertTrue(goPath.getGoPathEntries().size() == 0);
		assertEquals(goPath.getGoPathWorkspaceString(), "");
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		GoEnvironment goEnv = SAMPLE_GOEnv_1;
		
		assertAreEqual(goEnv.getGoOS_GoArch_segment(), "windows_386");
		
		Path goRoot_Source = goEnv.getGoRoot_Path().resolve("src/pkg");
		
		assertAreEqual(goEnv.findGoPackageForSourceModule(goRoot_Source.resolve("pack/m.go")), path("pack"));
		assertAreEqual(goEnv.findGoPackageForSourceModule(goRoot_Source.resolve("pack/foo/m.go")), path("pack/foo"));
		assertAreEqual(goEnv.findGoPackageForSourceModule(goRoot_Source.resolve("../foo/m.go")), null);
	}
	
}