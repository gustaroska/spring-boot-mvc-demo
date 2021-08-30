package com.demo;

public class ResponseWrapperList extends ResponseWrapper {

	private long count;
	private boolean nextMore = false;
	private String nextPage = "";
	private int nextPageNumber = 2;
	

	public ResponseWrapperList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseWrapperList(Object data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	public ResponseWrapperList(String status, int code) {
		super(status, code);
		// TODO Auto-generated constructor stub
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public boolean isNextMore() {
		return nextMore;
	}

	public void setNextMore(boolean nextMore) {
		this.nextMore = nextMore;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public int getNextPageNumber() {
		return nextPageNumber;
	}

	public void setNextPageNumber(int nextPageNumber) {
		this.nextPageNumber = nextPageNumber;
	}

}
