package br.com.unicred;

public class Starter {
	public static void main(String[] args) {
		Parser parser = new Parser();
		// parser.make();
		test();
	}

	public static void test() {
		String a[] = new String[] {
				"    compile group: 'br.com.unicred.us', name: 'credito-ufs-api', version: '1.1.0-SNAPSHOT', changing: true",
				"    compile group: 'br.com.unicred.us', name:    'credito-ufs-api', version: '1.1.0-SNAPSHOT', changing: true",
				"    compile group: 'br.com.unicred.us', name:'credito-ufs-api', version: '1.1.0-SNAPSHOT', changing: true" };
		System.out.println("size:" + a.length);
		for (String aa : a) {
			try {
				System.out.println(aa.split("name:\\W*")[1]);
			} catch (Exception e) {
			}
		}
	}

}
