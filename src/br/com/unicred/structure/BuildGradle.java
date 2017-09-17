package br.com.unicred.structure;

import java.util.HashSet;

public class BuildGradle {

	public String dependencies;
	public HashSet<String> testCompile = new HashSet<String>();
	public HashSet<String> compile = new HashSet<String>();
	public HashSet<String> classpath = new HashSet<String>();

}
