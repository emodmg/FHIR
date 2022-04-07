import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors


class Node implements Comparable<Node> {
	String firstName;
	String lastName;
	String birthdate;

	public Node(String a, String b, String c) {
		firstName = a;
		lastName = b;
		birthdate = c;
	}

	public String getFirstName() {
		return firstName;
	}

	public int compareTo(Node anotherInstance) {
        return this.firstName.compareTo(anotherInstance.firstName);// we can use this function to sort Arraylist of Nodes based on firstName
    }

    public void nodePrint() {
        System.out.println("Patient: " + firstName + " " + lastName + ".  birthdate: " + birthdate);
    }
}

public class SampleClient {

	public boolean firstNameExist(Arraylist<)

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();// creating a ctx
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response = client
        		.search()
                .forResource("Patient")
                .where(Patient.ADDRESS.matches().values("Ontario"))
                .returnBundle(Bundle.class)
                .execute();
//================================================================================
        System.out.println("Hello World");

        // initialize empty patients ArrayList
        List<IBaseResource> patients = new ArrayList<>();

        // search for all Patients completed, so we extract the first page
        patients.addAll(BundleUtil.toListOfResources(fhirContext, response));

        // Load the subsequent pages        
        while (response.getLink(IBaseBundle.LINK_NEXT) != null) {
        	response = client
        	.loadPage()
        	.next(response)
        	.execute();
        	patients.addAll(BundleUtil.toListOfResources(fhirContext, response));
        }


        // now we iterate through the Arraylist of patients
        // and store the critical info into Arraylist of Nodes
        List<Node> arr_node = new ArrayList<Node>();
        int PATIENT_NUM = 20;

        for (int i = 0; i < PATIENT_NUM; i++) {
        	IBaseResource p = patients.get(i);
        	p_firstName = p.getName().get(0).getGiven();
        	boolean exist = 0;// indicator for if firstName exist in node arraylist

        	// now we check if this patient p's last name already showed up in our Node Arraylist
        	for (Node i : arr_node) {
        		if (i.getFirstName().equals(p_firstName)) {
        			exist = 1;
        		}
        	}
        	if (exist == 1) {
        		continue;// we skip this patient cuz his/her firstName showed up before
        	}

        	// construct the Node for each patient using firstName, lastName, birthdate 
        	Node node = new Node(p.getName().get(0).getGiven(), p.getName().get(0).getFamily(), p.getBirthdate()); // I tried my best but I am still not sure about the API in FHIR, this getBirthdate should be wrong...
        	arr_node.add(node);
        }
        // now we get Arraylist containing 20 patient info

        // sort Arraylist based on firstName
        Collections.sort(arr_node);

        // create a string containing 20 first names from Arraylist
        String 20firstNameString;
        for (Node n : arr_node) {
        	20firstNameString += "\n" + n.getFirstName();
        }

        // create an output text file
        try {
        	File myObj = new File("20firstNames.txt");
        	if (myObj.createNewFile()) {
        		System.out.println("File created: " + myObj.getName());
        	} else {
        		System.out.println("File already exists.");
        	}
        } catch (IOException e) {
        	System.out.println("An error occurred.");
        	e.printStackTrace();
        }

        // write 20 first names to the text file
        try {
        	FileWriter myWriter = new FileWriter("20_firstNames.txt");
        	myWriter.write(20firstNameString);
        	myWriter.close();
        	System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
        	System.out.println("An error occurred.");
        	e.printStackTrace();
        }

        // now we have the text file with 20 different first name string
        // we can reads in the contents of this file and for each last name 
        //   queries for patients with that last name
    }

}
