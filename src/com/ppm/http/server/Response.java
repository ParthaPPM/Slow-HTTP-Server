package com.ppm.http.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Response
{
	private final String version;
	private final int responseCode;
	private final Map<String, String> headers;
	private final byte[] body;

	public Response()
	{
		this(404, null, null);
	}

	public Response(byte[] body)
	{
		this(200, null, body);
	}

	public Response(int responseCode, byte[] body)
	{
		this(responseCode, null, body);
	}

	public Response(Map<String, String> headers, byte[] body)
	{
		this(200, headers, body);
	}

	public Response(int responseCode, Map<String, String> headers, byte[] body)
	{
		this.version = "HTTP/1.1";
		this.responseCode = responseCode;
		this.headers = new HashMap<>();
		this.headers.putAll((headers==null) ? new HashMap<>() : headers);
		this.headers.put("Date", (new Date()).toString());
		this.headers.put("Server", "Nebula");
		this.headers.put("Connection", "keep-alive");
		this.headers.put("Content-Length", (body==null) ? "0" : String.valueOf(body.length));
		this.body = (body == null) ? new byte[]{} : body;
	}

	String version()
	{
		return version;
	}

	int responseCode()
	{
		return responseCode;
	}

	String responseCodeText()
	{
		return switch (responseCode)
		{
			case 100 -> "Continue";
			case 101 -> "Switching Protocol";
			case 102 -> "Processing";
			case 103 -> "Early Hints";
			case 200 -> "OK";
			case 201 -> "Created";
			case 202 -> "Accepted";
			case 203 -> "Non-Authoritative Information";
			case 204 -> "No Content";
			case 205 -> "Reset Content";
			case 206 -> "Partial Content";
			case 207 -> "Multi-Status";
			case 208 -> "Already Reported";
			case 226 -> "IM Used";
			case 300 -> "Multiple Choice";
			case 301 -> "Moved Permanently";
			case 302 -> "Found";
			case 303 -> "See Other";
			case 304 -> "Not Modified";
			case 305 -> "Use Proxy";
			case 306 -> "unused";
			case 307 -> "Temporary Redirect";
			case 308 -> "Permanent Redirect";
			case 400 -> "Bad Request";
			case 401 -> "Unauthorized";
			case 402 -> "Payment Required";
			case 403 -> "Forbidden";
			case 404 -> "Not Found";
			case 405 -> "Method Not Allowed";
			case 406 -> "Not Acceptable";
			case 407 -> "Proxy Authentication Required";
			case 408 -> "Request Timeout";
			case 409 -> "Conflict";
			case 410 -> "Gone";
			case 411 -> "Length Required";
			case 412 -> "Precondition Failed";
			case 413 -> "Payload Too Large";
			case 414 -> "URI Too Long";
			case 415 -> "Unsupported Media Type";
			case 416 -> "Range Not Satisfiable";
			case 417 -> "Expectation Failed";
			case 418 -> "I'm a teapot";
			case 421 -> "Misdirected Request";
			case 422 -> "Unprocessable Entity";
			case 423 -> "Locked";
			case 424 -> "Failed Dependency";
			case 425 -> "Too Early";
			case 426 -> "Upgrade Required";
			case 428 -> "Precondition Required";
			case 429 -> "Too Many Requests";
			case 431 -> "Request Header Fields Too Large";
			case 451 -> "Unavailable For Legal Reasons";
			case 500 -> "Internal Server Error";
			case 501 -> "Not Implemented";
			case 502 -> "Bad Gateway";
			case 503 -> "Service Unavailable";
			case 504 -> "Gateway Timeout";
			case 505 -> "HTTP Version Not Supported";
			case 506 -> "Variant Also Negotiates";
			case 507 -> "Insufficient Storage";
			case 508 -> "Loop Detected";
			case 510 -> "Not Extended";
			case 511 -> "Network Authentication Required";
			default -> "Unknown code";
		};
	}

	Map<String, String> headers()
	{
		return headers;
	}

	byte[] body()
	{
		return body;
	}
}