package br.com.unicred.structure;

import java.util.HashSet;

public class Project {
	public BuildGradle buildGradle = new BuildGradle();
	public HashSet<String> usedByProjects = new HashSet<String>();
}
