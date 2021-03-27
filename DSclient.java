    import java.net.*;  
    import java.io.*;  
	import java.nio.charset.StandardCharsets;
    class DSclient{  

		//Collection of Strings saved in order to transform to bytes later
		static final String HELO = "HELO";
		static final String OK   = "OK";
		static final String REDY = "REDY";
		static final String GETALL = "GETS All";

	    public static void main(String args[])throws Exception{ 
			Socket s=new Socket("localhost",50000);  
			DataInputStream din=new DataInputStream(s.getInputStream());  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			String TempStringResponse = "sup";
			String TempStringResponse2 = "sup";
			String TempStringResponse3 = "sup";
			String TempStringResponse4 = "sup";
			String TempStringResponse5 = "sup";
			String MyTest = "ok";

			//Beginning of Authentication process with HELO
			dout.write(HELO.getBytes());
			dout.flush();

			byte[] ResponseBytes = new byte[2]; // waiting for the OK
			din.read(ResponseBytes);
			TempStringResponse = new String(ResponseBytes, StandardCharsets.UTF_8);
			System.out.println("Server: " + TempStringResponse);


			//Send in AUTH and name
			MyTest = "AUTH Brendon";
			dout.write(MyTest.getBytes());
			dout.flush();

			byte[] ResponseBytes2 = new byte[2]; // waiting for the OK with welcomes
			din.read(ResponseBytes2);
			TempStringResponse2 = new String(ResponseBytes, StandardCharsets.UTF_8);
			System.out.println("Server: " + TempStringResponse2);

			//Time to say REDY
			dout.write(REDY.getBytes());
			dout.flush();

			byte[] groupofbytes = new byte[5]; // Server resposne
			din.read(groupofbytes);
			TempStringResponse3 = new String(groupofbytes, StandardCharsets.UTF_8);
			System.out.println("Server: " + TempStringResponse3);

			//get all test
			dout.write(GETALL.getBytes());
			dout.flush();

			//get all response
			byte[] groupofbytes1 = new byte[12];
			din.read(groupofbytes1);
			TempStringResponse4 = new String(groupofbytes1, StandardCharsets.UTF_8);
			System.out.println("Server: " + TempStringResponse4);

			//get all test
			dout.write(OK.getBytes());
			dout.flush();
			
			//get all response
			byte[] groupofbytes2 = new byte[184*124];
			din.read(groupofbytes2);
			TempStringResponse5 = new String(groupofbytes1, StandardCharsets.UTF_8);
			System.out.println("Server: " + TempStringResponse5);


			//get all test
			dout.write(OK.getBytes());
			dout.flush();



			/*
			String str="",str2="";  
			while(true)
				{  
					str=br.readLine();  
					dout.writeUTF(str);  
					dout.flush();  
					str2=din.readUTF();  
					if(str2.equals("BYE!")){
						System.out.println("Server says: "+str2);
					dout.close();  
					s.close();
					break;  
					}  else {
						System.out.println("Server says: "+str2);
					}
				

				}  
				*/
	    }
    }  
