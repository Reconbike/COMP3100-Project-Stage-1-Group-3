    import java.net.*;  
    import java.io.*;  
	import java.nio.charset.StandardCharsets;
    class DSclient{  

		//Collection of Strings saved in order to transform to bytes later
		static final String HELO = "HELO";
		static final String OK   = "OK";
		static final String REDY = "REDY";
		static final String GETALL = "GETS All";
		static final String QUIT = "QUIT";

	    public static void main(String args[])throws Exception{ 
			Socket s=new Socket("localhost",50000);  
			DataInputStream din=new DataInputStream(s.getInputStream());  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			String TempStringResponse;

			//Beginning of Authentication process with HELO
			dout.write(HELO.getBytes());
			dout.flush();

			// waiting for the OK
			byte[] ResponseBytes = new byte[2];
			din.read(ResponseBytes);
			TempStringResponse = new String(ResponseBytes, StandardCharsets.UTF_8);
			System.out.println("Server: " + TempStringResponse);


			//Send in AUTH and name
			String Username = "AUTH Brendon";
			dout.write(Username.getBytes());
			dout.flush();

			// waiting for the OK with welcomes
			byte[] ResponseBytes2 = new byte[2];
			din.read(ResponseBytes2);
			TempStringResponse = new String(ResponseBytes2, StandardCharsets.UTF_8);
			System.out.println("Server: " + TempStringResponse);

			//Time to say REDY
			dout.write(REDY.getBytes());
			dout.flush();

			//Are there tasks left from server
			Boolean tasksLeft = true;

			//Fully concatenated SCHD command
			String SCHD;

			//Largest server name and id (eg. x-large 3)
			String largestServer = null;

			while (tasksLeft)
				{
					//Server response
					byte[] groupofbytes = new byte[50]; 
					din.read(groupofbytes);
					TempStringResponse = new String(groupofbytes, StandardCharsets.UTF_8);
					System.out.println("Server: " + TempStringResponse);

					//If non tasks left change boolean value
					if(TempStringResponse.substring(0, 4).equals("NONE")){
						tasksLeft = false;
					}

					//Split job values
					if(TempStringResponse.substring(0,4).equals("JOBN")){

						String[] JOBNsplit = TempStringResponse.split("\\s+");
						int JobSubmitTime = Integer.parseInt(JOBNsplit[1]);
						int JobID = Integer.parseInt(JOBNsplit[2]);
						int JobRunTime = Integer.parseInt(JOBNsplit[3]);
						int JobCores = Integer.parseInt(JOBNsplit[4]);
						int JobMemory = Integer.parseInt(JOBNsplit[5]);
						//int JobDisk = Integer.parseInt(JOBNsplit[6]);
					
						
						/*System.out.println(JOBNsplit[0]);
						System.out.println(JOBNsplit[1]);
						System.out.println(JOBNsplit[2]);
						System.out.println(JOBNsplit[3]);
						System.out.println(JOBNsplit[4]);
*/
						//Send GetALl request
						dout.write(GETALL.getBytes());
						dout.flush();
	
						//get all response
						byte[] groupofbytes1 = new byte[12];
						din.read(groupofbytes1);
						TempStringResponse = new String(groupofbytes1, StandardCharsets.UTF_8);
						System.out.println("Server: " + TempStringResponse);
	
						//get all test
						dout.write(OK.getBytes());
						dout.flush();
						
						//get all response
						byte[] groupofbytes2 = new byte[184*124];
						din.read(groupofbytes2);
						TempStringResponse = new String(groupofbytes2, StandardCharsets.UTF_8);
						System.out.println("Server: " + TempStringResponse);

						//Find largest server (Largest = first listed server with highest number of cpu cores)
						if(largestServer == null){
							String[] ServerSplit = TempStringResponse.split("\\n+");
							String biggestServer = ServerSplit[0];
	
							for(int i = 0; i < ServerSplit.length; i++){
								String[] ServerSplitWord = ServerSplit[i].split("\\s+");
								String[] BigSplitWord = biggestServer.split("\\s+");
								int currentCore = Integer.parseInt(ServerSplitWord[4]);
								int bestCore = Integer.parseInt(BigSplitWord[4]);
								if(currentCore > bestCore){
									biggestServer = ServerSplit[i];
								}
							}
	
							String[] bigSplit = biggestServer.split("\\s+");
							largestServer = bigSplit[0] + " " + bigSplit[1];
						}
	
						//get all test
						dout.write(OK.getBytes());
						dout.flush();

						//Waiting for period/dot server response
						byte[] ResponseBytes3 = new byte[1]; 
						din.read(ResponseBytes3);
						TempStringResponse = new String(ResponseBytes3, StandardCharsets.UTF_8);
						System.out.println("Server: " + TempStringResponse);
	
						//Send scheduling message (all to largest server)
						SCHD = "SCHD " + JobID + " " + largestServer;
						System.out.println(SCHD);
						dout.write(SCHD.getBytes());
						dout.flush();

						//Waiting for ok 
						byte[] ResponseBytes4 = new byte[2];
						din.read(ResponseBytes4);
						TempStringResponse = new String(ResponseBytes4, StandardCharsets.UTF_8);
						System.out.println("Server: " + TempStringResponse);
					}
					

					//Time to say REDY for next job
					dout.write(REDY.getBytes());
					dout.flush();

				}

				//Send quit message to server
				dout.write(QUIT.getBytes());
				dout.flush();

				//Waiting for final ok
				byte[] ResponseBytes5 = new byte[4]; 
				din.read(ResponseBytes5);
				TempStringResponse = new String(ResponseBytes5, StandardCharsets.UTF_8);
				System.out.println("Server: " + TempStringResponse);




	    }
    }  
