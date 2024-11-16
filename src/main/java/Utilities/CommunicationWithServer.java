package Utilities;


/**
     *
     * @author agarc
     */
    public class CommunicationWithServer {

     /*   public static Socket connectToServer(String IpAddress) throws IOException {
            Socket socket = new Socket();
            socket = new Socket(IpAddress, 9000);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            return socket;
        }

        public static void sendDoctor(PrintWriter pw, Doctor doctor) {
            pw.println(doctor.toString());
        }

        public static void sendPatient(PrintWriter pw,Patient patient) {
            pw.println(patient.toString());
        }

        public static void sendSignal(PrintWriter printWriter, Signal signal) {
            printWriter.println(signal.toString());
        }

        public static void sendUser(PrintWriter printWriter, User user) {
            printWriter.println(user.toString());
        }

        public static Patient receivePatient(BufferedReader bf) {
            Patient p = new Patient();

            try{
                String line = bf.readLine();
                line=line.replace("{", "");
                line=line.replace("Patient", "");
                line=line.replace("}", "");
                String[] atribute = line.split(",");
                SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yyyy");

                for (int i =0;i <atribute.length; i++){
                    String[] data2 = atribute[i].split("=");
                    for (int j =0;j <data2.length - 1; j++){
                        data2[j]=data2[j].replace(" ", "");
                        switch(data2[j]){
                            case "medical_card_number":
                                p.setMedical_card_number(Integer.parseInt(data2[j+1]));
                                break;
                            case "name":
                                p.setName(data2[j+1]);
                                break;
                            case "surname":
                                p.setSurname(data2[j+1]);
                                break;
                            case "dob":
                                //Date dob = java.sql.Date.valueOf(data2[j+1]);
                                //p.setDob(dob);
                                try {
                                    p.setDob(format.parse(data2[j+1]));
                                } catch (ParseException ex) {
                                    Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;



                            case "address":
                                p.setAddress(data2[j+1]);
                                break;
                            case "email":
                                p.setEmail(data2[j+1]);
                                break;
                            case "diagnosis":
                                p.setDiagnosis(data2[j+1]);
                                break;
                            case "allergies":
                                p.setAllergies(data2[j+1]);
                                break;
                            case "gender":
                                p.setGender(data2[j+1]);
                                break;

                            case "userId":
                                p.setUserId(Integer.parseInt(data2[j+1]));
                                break;
                            case "macAddress":
                                p.setMacAddress(data2[j+1]);
                                break;
                        }
                    }
                }
                return p;
            }catch(IOException ex){
                return  null;
            }catch(NotBoundException e){
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, e);
                return null;
            }
        }


        public static Doctor receiveDoctor(BufferedReader bufferReader){
            Doctor d= new Doctor();
            try{
                String line = bufferReader.readLine();
                line=line.replace("{", "");
                line=line.replace("Doctor", "");
                line=line.replace("}", "");
                String[] atribute = line.split(",");
                for (int i =0;i <atribute.length; i++){
                    String[] data2 = atribute[i].split("=");
                    for (int j =0;j <data2.length - 1; j++){
                        data2[j]=data2[j].replace(" ", "");
                        switch(data2[j]){
                            case "name":d.setDname(data2[j+1]);
                                break;
                            case "surname":d.setDsurname(data2[j+1]);
                                break;
                            case "email": d.setDemail(data2[j+1]);
                                break;
                            case "id":d.setDoctorId(Integer.parseInt(data2[j+1]));
                                break;
                        }
                    }
                }
            }catch(IOException ex){
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return d;
        }


        public static Signal receiveECGSignal(BufferedReader br) throws IOException {
            Signal s = new Signal();

            String line = br.readLine();
            System.out.println(line);
            line = line.replace("{", "");
            line = line.replace("Signal", "");
            String[] atribute = line.split("/");

            for (int i = 0; i < atribute.length; i++) {
                String[] data2 = atribute[i].split("=");
                for (int j = 0; j < data2.length - 1; j++) {
                    data2[j] = data2[j].replace(" ", "");
                    if(data2[j].equalsIgnoreCase("ECG_values")) {
                        data2[j+1]=data2[j+1].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
                        String[] separatedString;
                        separatedString = data2[j + 1].split(",");
                        List<Integer> ECG = new ArrayList();
                        for (int k = 0; k < separatedString.length; k++) {
                            ECG.add(k, Integer.parseInt(separatedString[k]));
                        }
                        s.setECG_values(ECG);
                    }
                }
            }
            return s;
        }

        public static Signal receiveEMGSignal(BufferedReader br) throws IOException {
            Signal s = new Signal();
            String line = br.readLine();
            line = line.replace("{", "");
            line = line.replace("Signal", "");
            String[] atribute = line.split("/");
            for (int i = 0; i < atribute.length; i++) {
                String[] data2 = atribute[i].split("=");
                for (int j = 0; j < data2.length - 1; j++) {
                    data2[j] = data2[j].replace(" ", "");
                    if(data2[j].equalsIgnoreCase("EMG_values")) {
                        data2[j+1]=data2[j+1].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
                        String[] separatedString;
                        separatedString = data2[j + 1].split(",");
                        List<Integer> EMG = new ArrayList();
                        for (int k = 0; k < separatedString.length; k++) {
                            EMG.add(k, Integer.parseInt(separatedString[k]));
                        }
                        s.setEMG_values(EMG);
                    }
                }
            }
            return s;
        }

        public static User receiveUser (BufferedReader br){
            User u = new User();
            try {
                String line = br.readLine();
                line=line.replace("{", "");
                line=line.replace("User", "");
                line=line.replace("}", "");
                String[] atribute = line.split(",");
                for (int i =0;i <atribute.length; i++){
                    String[] data2 = atribute[i].split("=");
                    for (int j =0;j <data2.length - 1; j++){
                        data2[j]=data2[j].replace(" ", "");
                        switch(data2[j]){
                            case "username":
                                u.setUsername(data2[j+1]);
                                break;
                            case "password":
                                u.setPassword(data2[j+1]);
                                break;
                            case "role":
                                u.setRole(Integer.parseInt(data2[j+1]));
                                break;
                            case "userId":
                                u.setUserId(Integer.parseInt(data2[j+1]));
                                break;
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return u;
        }


        public static List<String> ShowSignals(BufferedReader bf, PrintWriter pw){
            try {
                List<String> filenames = new ArrayList();

                int size = Integer.parseInt(bf.readLine()); //aqui coge el size que se envia
                for (int i=0; i<size; i++){
                    filenames.add(bf.readLine());
                }
                return filenames;
            } catch (IOException ex) {
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }


        public static List<String> receivePatientList(BufferedReader bf){
            List<String> patientList = new ArrayList();
            boolean stop= true;
            try {
                while(stop){
                    String line = bf.readLine();
                    if (!line.equalsIgnoreCase("End of list")) {
                        stop=true;
                        patientList.add(line);
                    }else{
                        stop=false;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return patientList;
        }

        public static void exitFromServer(InputStream inputStream, OutputStream outputStream, Socket socket ){
            try{
                inputStream.close();
            }catch(IOException ex){
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try{
                outputStream.close();
            }catch(IOException ex){
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try{
                socket.close();
            }catch(IOException ex){
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public static boolean ReleaseResources(PrintWriter pw, BufferedReader br) {
            pw.close();
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(CommunicationWithServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }*/




}
