/*
Copyright 2011-2015 Stefano Cappa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package logica;

public class Os {
	private String osName;
	private String osVersion;
	private String architecture;

	public Os() {
		this.osName = System.getProperty("os.name");
		this.osVersion = System.getProperty("os.version"); 
	}

	public String getOsName() {
		return osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getArchitecture() {
		return architecture;
	}

	public boolean is64Bit() {
		return this.architecture.contains("64");
	}

	public boolean isItunesCompatible() {
		return osName.contains("Mac") || osName.contains("Windows");
	}

	public boolean isLinux() {
		return osName.contains("Linux");
	}

	public boolean isMac() {
		return osName.contains("Mac");
	}

	public boolean isWindows() {
		return osName.contains("Windows");
	}
}
