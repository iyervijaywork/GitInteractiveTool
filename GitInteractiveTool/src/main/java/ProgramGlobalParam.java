class ProgramGlobalParam 
{ 
    private static ProgramGlobalParam single_instance = null; 
    private int rate; 
    private String token;
    private Boolean sleepForRate;
    private int maxRepoParseCount;
    
    private ProgramGlobalParam() 
    { 
        rate = 0;
        setToken(null);
        setSleepForRate(false);
        maxRepoParseCount = Integer.MAX_VALUE; 
    } 

    public static ProgramGlobalParam getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new ProgramGlobalParam(); 
  
        return single_instance; 
    } 
    public void DecrementRate(){
    		rate--;
    		//System.out.println(count);
    }

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getSleepForRate() {
		return sleepForRate;
	}

	public void setSleepForRate(Boolean sleepForRate) {
		this.sleepForRate = sleepForRate;
	}

	public int getMaxRepoParseCount() {
		return maxRepoParseCount;
	}

	public void setMaxRepoParseCount(int maxRepoParseCount) {
		this.maxRepoParseCount = maxRepoParseCount;
	}
} 