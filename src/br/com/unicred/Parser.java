package br.com.unicred;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;

import br.com.unicred.structure.BuildGradle;
import br.com.unicred.structure.NameSpace;
import br.com.unicred.structure.Project;
import br.com.unicred.structure.Service;

public class Parser {

	// https://www.freeformatter.com/json-formatter.html
	//
	// WIN
	// dir /S /B build.gradle > info.txt
	//
	// Linux
	//

	private static String BASE_PATH = "C:\\Users\\will\\Downloads\\services\\";
	private static final String INFO_TXT = "info.txt";
	private static final String OUT_JSON = "out.json";

	private static final String BUILD_GRADLE = "build.gradle";

	public void make() {
		HashMap<String, NameSpace> namespaces = new HashMap<>();

		// auxiliar para ser utilido no processo de 'insert usedByProjects'
		HashMap<String, HashSet<String>> usedByProjects = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(new File(BASE_PATH, INFO_TXT).getAbsolutePath()))) {
			String line;
			while ((line = br.readLine()) != null) {
				String splited[] = line.replace(BASE_PATH, "").split(File.separator + File.separator);
				String nameSpaceFromLine = splited[0];
				String serviceFromLine = splited[1];
				// pode ser project OU build.gradle
				String projectFromLine = splited[2];

				NameSpace namespace = namespaces.get(nameSpaceFromLine);
				if (namespace == null) {
					namespace = new NameSpace();
					namespaces.put(nameSpaceFromLine, namespace);
				}
				Service service = namespace.services.get(serviceFromLine);
				if (service == null) {
					service = new Service();
					namespace.services.put(serviceFromLine, service);
				}
				if (projectFromLine.equalsIgnoreCase(BUILD_GRADLE)) {
					service.buildGradle = createBuildGradle(line);
				} else {
					Project project = service.projects.get(projectFromLine);
					if (project == null) {
						project = new Project();
						service.projects.put(projectFromLine, project);
					}
					project.buildGradle = createBuildGradle(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		writeIntoJsonFile(gson.toJson(namespaces), "");
		// insere 'usedByProjects'
		insertUsedByProjects(namespaces);
		writeIntoJsonFile(gson.toJson(namespaces), "2");
	}

	private void insertUsedByProjects(HashMap<String, NameSpace> namespaces) {
		Iterator it = namespaces.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

		}
	}

	private BuildGradle createBuildGradle(String filePath) {
		BuildGradle buildGradle = new BuildGradle();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("testCompile")) {
					buildGradle.testCompile.add(line.split("testCompile\\s*")[1]);
				} else if (line.contains("compile")) {
					try {
						buildGradle.compile.add(line.split("compile\\W")[1]);
						// se estiver utilizando um projeto como dependencia
						if (line.contains("compile group") && line.contains("br.com.unicred")) {

						}

					} catch (Exception e) {
						System.out.println(filePath + "\nIGNORE: " + line);
					}
				} else if (line.contains("classpath")) {
					buildGradle.classpath.add(line.split("classpath\\s*")[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buildGradle;
	}

	private void writeIntoJsonFile(final String json, String sufix) {
		File jsonFile = new File(BASE_PATH, OUT_JSON + sufix);
		System.out.println("\nJSON:\n" + json + "\n" + jsonFile.getAbsolutePath());
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsonFile), "UTF-8"));
			try {
				out.write(json);
			} finally {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	private String readBuildGradle(String filePath) {
		StringBuffer fileContents = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				fileContents.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContents.toString().split("dependencies\\s*\\{\\s")[1].split("\\}")[0];
	}

}
