package com.acgist.hadoop.seze;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.util.Utf8;

public class SezeTest {

	public static void main(String[] args) throws IOException {
		Schema.Parser parser = new Schema.Parser();
		Schema schema = parser.parse(SezeTest.class.getResourceAsStream("hadoop.avsc"));
		GenericRecord record = new GenericData.Record(schema);
		record.put("left", new Utf8("L"));
		record.put("right", new Utf8("R"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
//		DataFileWriter<GenericRecord> fileWriter = new DataFileWriter<>(writer);
		Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
		writer.write(record, encoder);
		encoder.flush();
		out.close();
		System.out.println(out.toString());
		System.out.println("OK");
	}
	
}
