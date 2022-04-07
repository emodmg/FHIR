import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

class Node implements Comparable<Node> {
	String firstName;
	String lastName;
	String birthdate;

	public Node(String a, String b, String c) {
		firstName = a;
		lastName = b;
		birthdate = c;
	}

	public int compareTo(Node anotherInstance) {
        return this.firstName.compareTo(anotherInstance.firstName);// we can use this function to sort Arraylist of Nodes based on firstName
    }

    public void nodePrint() {
        System.out.println("Patient: " + firstName + " " + lastName + ".  birthdate: " + birthdate);
    }
}

public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();// creating a ctx
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response = client
        		.search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH"))
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

        for (IBaseResource p : patients) {
        	// construct the Node for each patient using firstName, lastName, birthdate 
        	Node node = new Node(p.getName().get(0).getGiven(), p.getName().get(0).getFamily(), p.getBirthdate()); // I tried my best but I am still not sure about the API in FHIR, this getBirthdate should be wrong...
        	arr_node.add(node);
        }

        // sort Arraylist based on firstName
        Collections.sort(arr_node);

        // print the patient info
        for (Node n : arr_node) {
            n.nodePrint();
        }
    }

}
