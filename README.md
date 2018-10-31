# mms-pdu-manager
A simple Java library for parsing and composing PDUs

The code for this library was extracted from the Android Open Source Project (AOSP) and slightly modified to remove all Android-specific functionalities to make it platform-agnostic so that it can be used on any Java-based application.

This repository also contains a sample project that demonstrates how to parse and compose PDUs from and into byte arrays respectively.

To compose a PDU into a byte array to send to an MMS Service Provider:

```java
// First instantiate the PDU we will be turning into a byte array
SendReq sendRequestPdu = new SendReq();
// Set the recipient number of our MMS
sendRequestPdu.addTo(new EncodedStringValue("555-555-5555"));
// Instantiate the body of our MMS
PduBody pduBody = new PduBody();
// Attach a sample image to our MMS body
byte[] sampleImageData = getBytesFromSampleImage();
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
// Send the pduData to the service provider
```

To parse a PDU from a byte array we receive from an MMS Service Provider:

```java
// First, load the bytes of our sample PDU that we usually get from the service
// provider when receiving an MMS
byte[] samplePduData = getBytesFromServiceProvider();
// Parse the byte array into a PDU object we can process
PduParser pduParser = new PduParser(samplePduData);
GenericPdu genericPdu = pduParser.parse();
// Cast the PDU to a more specific type. In this case, a NotificationInd PDU type.
NotificationInd resultPdu = (NotificationInd) genericPdu;
```
