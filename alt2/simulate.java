import java.io.*;
import java.util.*;


class CommandLineTable {
    private static final String HORIZONTAL_SEP = "-";
    private String verticalSep;
    private String joinSep;
    private String[] headers;
    private List<String[]> rows = new ArrayList<>();
    private boolean rightAlign;

    public CommandLineTable() {
        setShowVerticalLines(false);
    }

    public void setRightAlign(boolean rightAlign) {
        this.rightAlign = rightAlign;
    }

    public void setShowVerticalLines(boolean showVerticalLines) {
        verticalSep = showVerticalLines ? "|" : "";
        joinSep = showVerticalLines ? "+" : " ";
    }

    public void setHeaders(String... headers) {
        this.headers = headers;
    }

    public void addRow(String... cells) {
        rows.add(cells);
    }

    public void print() {
        int[] maxWidths = headers != null ?
                Arrays.stream(headers).mapToInt(String::length).toArray() : null;

        for (String[] cells : rows) {
            if (maxWidths == null) {
                maxWidths = new int[cells.length];
            }
            if (cells.length != maxWidths.length) {
                throw new IllegalArgumentException("Number of row-cells and headers should be consistent");
            }
            for (int i = 0; i < cells.length; i++) {
                maxWidths[i] = Math.max(maxWidths[i], cells[i].length());
            }
        }

        if (headers != null) {
            printLine(maxWidths);
            printRow(headers, maxWidths);
            printLine(maxWidths);
        }
        for (String[] cells : rows) {
            printRow(cells, maxWidths);
        }
        if (headers != null) {
            printLine(maxWidths);
        }
    }

    private void printLine(int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            String line = String.join("", Collections.nCopies(columnWidths[i] +
                    verticalSep.length() + 1, HORIZONTAL_SEP));
            System.out.print(joinSep + line + (i == columnWidths.length - 1 ? joinSep : ""));
        }
        System.out.println();
    }

    private void printRow(String[] cells, int[] maxWidths) {
        for (int i = 0; i < cells.length; i++) {
            String s = cells[i];
            String verStrTemp = i == cells.length - 1 ? verticalSep : "";
            if (rightAlign) {
                System.out.printf("%s %" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
            } else {
                System.out.printf("%s %-" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
            }
        }
        System.out.println();
    }

}

class Event 
{
    public String type; 
    public int time; 
    int order; 
    public Event(String type,int clock_time,int order) 
    {   
        // Test
        // System.out.println("Event created");
        this.type=type; 
        this.time=clock_time; 
        // Change order to orderID
        this.order=order; 
    } 
    public void show() 
    { 
        System.out.println("("+type+","+time+",O"+order+")"); 
    } 
    public String getEvent() 
    { 
        return "("+type+","+time+",O"+order+")";
    }
}

class FEL
{
    //creating a eventlist
    ArrayList<Event> fel = new ArrayList<Event>();

    public void add(Event A){
        // System.out.println("FEL triggered");
        // System.out.println("Event time = "+A.time);

        float ctime = A.time;
        // By default we append new value at last 
        int Index=fel.size();
        
        // get updated index if there's any events in fel
        if(fel.size()>0){
             // index to put place Event in the list
            for(int i=0; i<fel.size();i++)
            {
                if(ctime<=fel.get(i).time) 
                    {Index=i; break;}
            }
            // furthur index adjustment
            // checking if smallest element is there in fel or if fel is empty
            if(Index<fel.size() && ctime==fel.get(Index).time){
                    // If existing event is P1 or P2
                    if (fel.get(Index).type.equals("P1") || fel.get(Index).type.equals("P2")) {
                        Index++;
                    }
                    // IF existing event is S1 or S2
                    else if(fel.get(Index).type.equals("S1") || fel.get(Index).type.equals("S2")){
                        // for parallel events, put them back following order number
                        if(A.type.equals("S1") || A.type.equals("S2")) Index++;
                        // for arrival event, put them back
                        else if(A.type=="A") Index++;
                        
                    }
            }
        }
        fel.add(Index,A);
    }
    public void show(){
        // System.out.println("Showing FEL");
        for(int i=0; i<fel.size();i++){
            fel.get(i).show();
        }
    }
    public Event remove(){
        if(fel.size()>0)
        return fel.remove(0);
        else
        return null;
    }
    public String getFEL(){
        // System.out.println("Showing FEL");
        String content = "";
        if (fel.size()==0) return "-";
        else {
            for(int i=0; i<fel.size();i++){
                content +=fel.get(i).getEvent()+",";
            }
            return content.substring(0,content.length()-1);
        }
    }
    public int size(){
        return fel.size();
    }
}

class Queue{
    ArrayList<Integer> q = new ArrayList<Integer>();
    public void add (int order){
        q.add(order);
    }
    public int remove(){
        if(q.size()>0){
            // int max = q.size()-1;
            return q.remove(0);
        }
        else return -1;
    }
    public void show(){
        if (q.size()==0) System.out.print("Empty");
        else 
        for(int i=0;i<q.size();i++){
            System.out.print(q.get(i)+" ");
        }
    }
    public String getQ(){
        String content="";
        if (q.size()==0) return "-";
        else 
        for(int i=0;i<q.size();i++){
            content += q.get(i)+",";
        }
        return content.substring(0,content.length()-1);
    }
}

// EXperiment Specific 
class State 
{ 
    static int sqt,pqt,s1t,s2t,p1t,p2t,clock;
}
class Stats{
    static int wait_time1 = 0; // Wait time at stage 1
    static int wait_time2 = 0; // wait time at stage 2
    static int busy_time_server1 = 0; 
    static int busy_time_server2 = 0; 
    static int busy_time_server3 = 0;
    static int busy_time_server4 = 0;
    static int idle_time_server1 = 0;
    static int idle_time_server2 = 0;
    static int idle_time_server3 = 0;
    static int idle_time_server4 = 0;
    static int max_server_queue = 0;
    static int max_packing_queue = 0;
    static int total_time = 0;

    static HashMap<Integer, Integer[]> t_data = new HashMap<Integer, Integer[]>();

    public static void show(){
        System.out.println("\n+------------------------------------------------------------------------------+");
        System.out.println("                  CUMULATIVE  STATISTICS  [sample = "+GLOBAL.SAMPLE_SIZE+" customers]");
        System.out.println("+------------------------------------------------------------------------------+");
        System.out.println("Max Server Queue Length = "+Stats.max_server_queue); 
        System.out.println("Max Packer Queue Length = "+Stats.max_packing_queue);
        // Total Time in system
        System.out.print("Total Time in system = "+Stats.total_time+" secs = ");
        System.out.print(Stats.total_time/60+" mins = ");
        System.out.println(Stats.total_time/3600+" hrs");
        // Total Busy time for all 3 servers
        System.out.println("Total Busy time for Server1 = "+Stats.busy_time_server1+" secs = "+Stats.busy_time_server1/60+" mins");
        System.out.println("Total Busy time for Server2 = "+Stats.busy_time_server2+" secs = "+Stats.busy_time_server2/60+" mins");
        System.out.println("Total Busy time for Server3 = "+Stats.busy_time_server3+" secs = "+Stats.busy_time_server3/60+" mins");
        System.out.println("Total Busy time for Server3 = "+Stats.busy_time_server4+" secs = "+Stats.busy_time_server4/60+" mins");
        // Server Utilization for all 3 servers
        System.out.println("Server 1 Utilization = "+ ((Stats.busy_time_server1*100)/Stats.total_time) +"%");
        System.out.println("Server 2 Utilization = "+ ((Stats.busy_time_server2*100)/Stats.total_time) +"%");
        System.out.println("Server 3 Utilization = "+ ((Stats.busy_time_server3*100)/Stats.total_time) +"%");
        System.out.println("Server 4 Utilization = "+ ((Stats.busy_time_server4*100)/Stats.total_time) +"%");
        // Total wait time for all orders at each stage
        System.out.println("Total Wait time at Stage 1 for all orders = "+Stats.wait_time1);
        System.out.println("Total Wait time at Stage 2 for all orders= "+Stats.wait_time2);
        // Average wait time in each stage
        System.out.print("Average waiting time per order at stage 1  = "+((int)(Stats.wait_time1)/t_data.size()) +" secs = ");
        System.out.println(((int)(Stats.wait_time1)/(t_data.size()*60)) + " mins");
        System.out.print("Average waiting time per order at stage 2  = "+((int)(Stats.wait_time2)/t_data.size()) +" secs = ");
        System.out.println(((int)(Stats.wait_time2)/(t_data.size()*60)) + " mins");
        // System.out.println("Average waiting time per order at stage 2   = "+((float)(Stats.wait_time2)/t_data.size()));

    }
    public static void show_t_data(){
        System.out.println("\nClock Time snapshot data for each order\nOrder|arrival|start 1|end 1|start 2|end 2");
        for (Integer i : Stats.t_data.keySet()) {
            System.out.print("["+i+"] ");
            for (int element: Stats.t_data.get(i)) 
                System.out.print(element+" ");
                System.out.println();
        }
    }

    public static void calculate_waiting_time(){
        
        for (Integer i : Stats.t_data.keySet()) {
            Integer[] temp = Stats.t_data.get(i);
            
            // IF start 1 - arrival > 0 there is a wait time
            if((temp[1] - temp[0])>0 ) {
                wait_time1 += temp[1] - temp[0];
            }
            // If start 2 - end 1 > 0 there is a wait time
            if((temp[3] - temp[2])>0 ) {
                wait_time2 += temp[3] - temp[2];
            }
        }
        
    }


}
class TIMESDATA 
{
    // public Queu order; 
    public Queue arrival = new Queue(); 
    public Queue service1 = new Queue(); 
    public Queue service2 = new Queue(); 
    public Queue service3 = new Queue();  
    public Queue service4 = new Queue(); 
    public TIMESDATA()
    {
        String line = "";  
        String splitBy = ",";  
        int count=0;
        try (BufferedReader br = new BufferedReader(new FileReader("times.csv"))) {
            while ((line = br.readLine()) != null)   
            { 
                if(count==0){
                    // Ignore first line
                    count++;
                    continue;
                }
            String[] times = line.split(splitBy);   
            int x0 = (int) (Float.parseFloat(times[0])*60); 
            int x1 = (int) (Float.parseFloat(times[1])*60); 
            int x2 = (int) (Float.parseFloat(times[2])*60); 
            int x3 = (int) (Float.parseFloat(times[3])*60); 
            int x4 = (int) (Float.parseFloat(times[3])*60); 

            this.arrival.add(x0);
            this.service1.add(x1);
            this.service2.add(x2);
            this.service3.add(x3);
            this.service4.add(x4);
            // debug
            // System.out.println("x1="+x1+",x2="+x2+",x3="+x3+",x3="+x4);
            }
        }   
        catch (IOException e){  
            e.printStackTrace();  
        } 

    }
    
    // public void show(){
    //     System.out.println("("+order+", "+arrival+", "+service1+", "+service2+", "+service3+")"); 
    // }
}

class GLOBAL{
    static Integer SAMPLE_SIZE = 10; // default sample = 10
    static String FILEPATH = "times.csv";
}

class simulate{
    public static void main (String args[]) throws FileNotFoundException{

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("\nEnter sample size (Max = 100) : ");  
            GLOBAL.SAMPLE_SIZE= sc.nextInt();
        }
        System.out.println("simulation started..."); 
        // Table setup
        CommandLineTable st = new CommandLineTable();
        st.setShowVerticalLines(true);
        // st.setHeaders("clock", "SQ(t)", "S1(t)","S2(t)","PQ(t)","P(t)","FEL","SQ","PQ");
        st.setHeaders("clock","SQ","S1","S2","PQ","P1","P2","FEL","SQ","PQ");
        // end table setup
        FEL fel=new FEL(); 
        TIMESDATA times = new TIMESDATA();
        Queue sq=new Queue() ; 
        Queue pq=new Queue() ; 
        initialise(fel,sq,pq,times,st); 
        st.print();
        System.out.println("simulation ended"); 

        Stats.calculate_waiting_time();
        // Stats.show_t_data();
        Stats.show();

    }

    public static void initialise(FEL fel,Queue sq,Queue pq,TIMESDATA times,CommandLineTable st) 
    { 
        State.sqt=0; 
        State.pqt=0; 
        State.s1t=1; 
        State.s2t=0; 
        State.p1t=0; 
        State.p2t=0; 
        State.clock=0; 
        // arrival time for order 2
        int nextOrderat = times.arrival.remove();
        //debug
        // System.out.println("2nd order arrives at="+nextOrderat);
        // service time for order 1
        int serviceEndsAt = times.service1.remove();
        fel.add(new Event("A",nextOrderat,2)); 
        fel.add(new Event("S1",serviceEndsAt,1)); 
        
        // Store Order 1 arrival in t_data
        Integer[] temp = {0, 0, 0, 0, 0};
        Stats.t_data.put(1,temp);

        start(fel,sq,pq,times,st); 
    }
    public static void display(FEL fel,Queue sq,Queue pq, CommandLineTable st){
        st.addRow(
            Integer.toString(State.clock), 
            Integer.toString(State.sqt), 
            Integer.toString(State.s1t), 
            Integer.toString(State.s2t), 
            Integer.toString(State.pqt), 
            Integer.toString(State.p1t), 
            Integer.toString(State.p2t), 
            fel.getFEL(),
            sq.getQ(),
            pq.getQ()
            ); 
    }
    public static void start(FEL fel,Queue sq,Queue pq, TIMESDATA times, CommandLineTable st) 
    { 
        // First display the current row => process the first (imminent) event
        display(fel,sq,pq,st);
        
        // Get imminent event from the fel
        Event imminent=fel.remove(); 
        
        // System.out.println("Showing the imminent event");
        // imminent.show();
        // System.out.println("Next event time ="+imminent.time);
        // System.out.println("Sample size ="+GLOBAL.SAMPLE_SIZE);
        
        // Time elapsed between this event and next imminent event
        int time_elapsed = imminent.time-State.clock;
        Stats.total_time +=time_elapsed;

        // Setting clock to next imminent event time
        State.clock=imminent.time; 

        // Storing current State info to calculate statistics after event processing
        int prev_s1t = State.s1t;
        int prev_s2t = State.s2t;
        int prev_p1t = State.p1t;
        int prev_p2t = State.p2t;
        
        // Processing emminet event which is One of 4 Events (Arrival,EndService1,EndService2,EndPacking)
        if(imminent.type.equals("A"))   arrival(fel,imminent,sq,pq,times); 
        if(imminent.type.equals("S1"))  endservice(fel,imminent,sq,pq,1,times); 
        if(imminent.type.equals("S2"))  endservice(fel,imminent,sq,pq,2,times); 
        if(imminent.type.equals("P1"))  endpacking(fel,imminent,sq,pq,1,times); 
        if(imminent.type.equals("P2"))  endpacking(fel,imminent,sq,pq,2,times); 
        
        // CALCULATING STATISTICS
        // Busy time for all servers
        if ((prev_s1t==1 && State.s1t==0) || (prev_s1t==1 && State.s1t==1)) Stats.busy_time_server1 += time_elapsed;
        if ((prev_s2t==1 && State.s2t==0) || (prev_s2t==1 && State.s2t==1)) Stats.busy_time_server2 += time_elapsed;
        if ((prev_p1t==1 && State.p1t==0) || (prev_p1t==1 && State.p1t==1)) Stats.busy_time_server3 += time_elapsed;
        if ((prev_p2t==1 && State.p2t==0) || (prev_p2t==1 && State.p2t==1)) Stats.busy_time_server4 += time_elapsed;

        // System.out.println("order = "+imminent.order+" sample="+GLOBAL.SAMPLE_SIZE+" and Event="+imminent.type);
        // Continue or end simulation check
        if(imminent.order>=GLOBAL.SAMPLE_SIZE &&  (imminent.type.equals("P1") || imminent.type.equals("P2")))   {
        // if(imminent.order>=GLOBAL.SAMPLE_SIZE+1 )   {
            fel.add(new Event("END-OF-SIM", imminent.time, imminent.order));
            display(fel,sq,pq,st);
        }
        else    {
            // System.out.println("condition not met");
            start(fel,sq,pq,times,st);
        }

    }
    public static void endservice(FEL fel,Event imminent,Queue sq,Queue pq, int server, TIMESDATA times) 
    { 
        
        // change server status
        if(server ==1) State.s1t--;
            else State.s2t--;

        // store service ends time of order i to t_data
        Integer[] temp = Stats.t_data.get(imminent.order);
        temp[2] = State.clock;
        Stats.t_data.put(imminent.order,temp);

        // Go to server 4 only if server 3 is busy
        if(State.p1t==1 && State.p2t==0) 
        {   
            // IF no order at packing
            State.p2t++;
            int packingEndsAt=State.clock+times.service4.remove();
            // System.out.print("packing ends at"+packingEndsAt);
            fel.add(new Event("P2",packingEndsAt,imminent.order));
            
            // store stage 2 start time to  t_data
            temp = Stats.t_data.get(imminent.order);
            temp[3] = State.clock;
            Stats.t_data.put(imminent.order,temp);
            
        } 
        // otherwise go to server 3
        else if(State.p1t==0) 
        {   
            // IF no order at packing
            State.p1t++;
            int packingEndsAt=State.clock+times.service3.remove();
            // System.out.print("packing ends at"+packingEndsAt);
            fel.add(new Event("P1",packingEndsAt,imminent.order));
            
            // store stage 2 start time to  t_data
            temp = Stats.t_data.get(imminent.order);
            temp[3] = State.clock;
            Stats.t_data.put(imminent.order,temp);
            
        } 
        // Else if both servers are busy
        else if(State.p1t==1 && State.p2t==1) 
        {   
            // If there's already an order at Packing add to queue
            State.pqt++;
            pq.add(imminent.order);
        } 

        // CHECK if there's any order in Server Queue before
        if(State.sqt!=0) 
        {   
            // Server the order waiting in queue
            // update server status
            if(server ==1) State.s1t++;
            else State.s2t++;

            State.sqt--;
            int order = sq.remove();

            if(server ==1) {
                int serviceEndsAt=State.clock+times.service1.remove();
                fel.add(new Event("S1",serviceEndsAt,order));
            }
            else {
                int serviceEndsAt=State.clock+times.service2.remove();
                fel.add(new Event("S2",serviceEndsAt,order));
            }
            
            // store service start time of order i in t_data
            temp = Stats.t_data.get(order);
            temp[1] = State.clock;
            Stats.t_data.put(order,temp);
        } 
        // Collect Statistics
        if (State.pqt > Stats.max_packing_queue){
            Stats.max_packing_queue = State.pqt;
        }
    } 
    public static void endpacking(FEL fel,Event imminent,Queue sq,Queue pq, int server, TIMESDATA times) 
    { 
        if(server ==1) State.p1t--;
        else State.p2t--;
        
        // Store stage 2 end time in t_data
        Integer[] temp = Stats.t_data.get(imminent.order);
        temp[4] = State.clock;
        Stats.t_data.put(imminent.order,temp);

        // If theres any one in the Pakcing Queuue start packing it
        if(State.pqt!=0) 
            {   
                State.pqt--;
                int order = pq.remove();
                
                if(server ==1) {
                    State.p1t++;
                    int packingEndsAt=State.clock+times.service3.remove();
                    fel.add(new Event("P1",packingEndsAt,order));
                }
                else {
                    State.p2t++;
                    int packingEndsAt=State.clock+times.service4.remove();
                    fel.add(new Event("P2",packingEndsAt,order));
                }

                // Store stage 2 start time in t_data
                temp = Stats.t_data.get(order);
                temp[3] = State.clock;
                Stats.t_data.put(order,temp);
            } 
    }
    public static void arrival(FEL fel,Event imminent,Queue sq,Queue pq, TIMESDATA times) 
    { 
        // some order arrived
        
        // **IMP** Store arrival info in t_data
        // {arr,stg1_st, stg1_end, stg2_start, stg2_end}
        Integer[] temp = {imminent.time, 0, 0, 0, 0};
        Stats.t_data.put(imminent.order,temp);
        
        // IF server 1 is busy and server 2 is idle go to server 2
        if(State.s1t==1 && State.s2t==0) 
        {
            State.s2t++;
            int order = imminent.order;
            int serviceEndsAt=State.clock+times.service2.remove();
            fel.add(new Event("S2",serviceEndsAt,order));
            
            // store stage 1 start time
            temp = Stats.t_data.get(imminent.order);
            temp[1] = State.clock;
            Stats.t_data.put(imminent.order,temp);
        } 
        else if(State.s1t==0){ 
            // If server 2 is busy and server 1 is idle go to server 1
            State.s1t++;
            int order = imminent.order;
            int serviceEndsAt=State.clock+times.service1.remove();
            fel.add(new Event("S1",serviceEndsAt,order));

            temp = Stats.t_data.get(imminent.order);
            temp[1] = State.clock;
            Stats.t_data.put(imminent.order,temp);
        }
        // IF both are busy
        else if(State.s1t==1 && State.s2t==1)
        {
            // no event schedules, order goes to queue
            State.sqt++;
            sq.add(imminent.order);

        }
        
        
        // Shedule new arrival
        if(imminent.order<100){
            int nextarrivalAt = times.arrival.remove();
            fel.add(new Event("A",State.clock+nextarrivalAt,imminent.order+1));
        }
        else{
            // don't shedule any further events
        }

        // Collect Stats
       if(State.sqt > Stats.max_server_queue){
        Stats.max_server_queue = State.sqt;
       }


    }


}

// A = new Event("A",0,1);