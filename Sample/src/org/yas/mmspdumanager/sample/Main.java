package org.yas.mmspdumanager.sample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.mms.pdu.EncodedStringValue;
import com.google.mms.pdu.GenericPdu;
import com.google.mms.pdu.PduBody;
import com.google.mms.pdu.PduComposer;
import com.google.mms.pdu.PduParser;
import com.google.mms.pdu.PduPart;
import com.google.mms.pdu.SendReq;

public class Main {

	private static final String SAMPLE_IMAGE_FILE_NAME = "mms.png";
	private static final String SAMPLE_PDU_FILE_NAME = "pdu.dat";

	public static void main(String[] args) throws IOException {
		// Create an MMS Send Request PDU
		sendMmsSample();
		// Read an MMS PDU
		readMmsSample();
	}

	private static void sendMmsSample() throws IOException {
		// First instantiate the SendReq PDU we will be turning into a byte array that
		// will be ultimately sent to the MMS service provider.
		SendReq sendRequestPdu = new SendReq();
		// Set the recipient number of our MMS
		sendRequestPdu.addTo(new EncodedStringValue("555-555-5555"));
		// Instantiate the body of our MMS
		PduBody pduBody = new PduBody();
		// Attach a sample image to our MMS body
		InputStream sampleImageStream = Main.class.getResourceAsStream(SAMPLE_IMAGE_FILE_NAME);
		byte[] sampleImageData = getBytesFromInputStream(sampleImageStream);
		PduPart sampleImagePduPart = new PduPart();
		sampleImagePduPart.setData(sampleImageData);
		sampleImagePduPart.setContentType("image/png".getBytes());
		sampleImagePduPart.setFilename(SAMPLE_IMAGE_FILE_NAME.getBytes());
		pduBody.addPart(sampleImagePduPart);
		// Set the body of our MMS
		sendRequestPdu.setBody(pduBody);
		// Finally, generate the byte array to send to the MMS provider
		PduComposer composer = new PduComposer(sendRequestPdu);
		byte[] pduData = composer.make();
		int pduDataLength = pduData.length;
		System.out.println(String.format("Successfully composed a PDU byte array of size: %,d bytes", pduDataLength));
	}

	private static void readMmsSample() throws IOException {
		// First, load the bytes of our sample PDU that we usually get from the service
		// provider when receiving an MMS
		InputStream samplePduStream = Main.class.getResourceAsStream(SAMPLE_PDU_FILE_NAME);
		byte[] samplePduData = getBytesFromInputStream(samplePduStream);
		// Parse the byte array into a PDU object we can process
		PduParser pduParser = new PduParser(samplePduData);
		GenericPdu genericPdu = pduParser.parse();
		// In this case we know that our sample PDU is of type SendReq, so we can cast
		// it to that.
		// Note that in this case we are loading the same SendReq PDU from the
		// sendMmsSample() method, but normally what we get from MMS service providers
		// are NotificationInd PDUs, RetrieveConf PDUs, and so on.
		SendReq sendRequestPdu = (SendReq) genericPdu;
		System.out.println(String.format("Successfully parsed a PDU with transaction ID: %s",
				new String(sendRequestPdu.getTransactionId())));
	}

	private static byte[] getBytesFromInputStream(InputStream inoutStream) throws IOException {
		ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (int numBytesRead = inoutStream.read(buffer); numBytesRead != -1; numBytesRead = inoutStream.read(buffer)) {
			bytesOutputStream.write(buffer, 0, numBytesRead);
		}
		return bytesOutputStream.toByteArray();
	}

}
