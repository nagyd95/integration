package integration.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class SectionRest {
  @Autowired
  private SectionRepository sectionRepository;

  @Autowired
  private SectionOngoingRepository sectionOngoingRepository;

  @GetMapping(path="/getccall" , produces = MediaType.APPLICATION_XML_VALUE)
  public List<CallResponse> getCcall(@RequestParam(value="caller") String caller,
                                     @RequestParam(value="called") String called,
                                     @RequestParam(required = false,value = "startdate") String startDate,
                                     @RequestParam(required = false,value = "enddate") String endDate ) throws ParseException {
    List<CallResponse> callResponses =new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date sDate = sdf.parse("1900-01-01");
    Date eDate = sdf.parse("2200-01-01");
    if(startDate!=null) {
       sDate=sdf.parse(startDate);

    }
    if(endDate!=null){
      eDate=sdf.parse(endDate);
    }

    List<Section> completedCalls=sectionRepository.findbyParticipant(caller,called,sDate,eDate);
     for(Section sec:completedCalls) {
      CallResponse callResponse = new CallResponse();
      callResponse.setCcdrId(sec.getCcdrId());
      callResponse.setCalledName(sec.getDestinationName());
      callResponse.setCallerName(sec.getSourceName());
      callResponse.setGlobalCallID(sec.getNativeId());
      callResponses.add(callResponse);
    }

    return callResponses;
  }

  @GetMapping(path="/getcall" , produces = MediaType.APPLICATION_XML_VALUE)
  public List<OngoingResponse> getCall(@RequestParam(value="caller") String caller,@RequestParam(value="called") String called) {
    List<OngoingResponse> response=new ArrayList<>();
    List<SectionOngoing> calls =sectionOngoingRepository.findBySourceAndDestinationCallerId(caller, called);
    OngoingResponse call = new OngoingResponse();
      if(calls.size()>0) {
        for(SectionOngoing so: calls) {
          call.setCcdrid(so.getCcdrId());
          call.setGlobalCallID(so.getNativeId());
          response.add(call);
        }
      }else{
        call.setError(true);
        call.setErrorReason("Caller: "+caller +", Called: "+called +" - No call in progress was found.  ");
        response.add(call);
      }
    return response;
  }

  @RequestMapping(path="/download",method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download(@RequestParam("ccdrid") String ccdrid) throws IOException {
    File file = new File("C:\\media\\"+getCcdr(ccdrid));
    Path path = Paths.get(file.getAbsolutePath());
    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Name","value");
    return  ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
  }

  private String getCcdr(String ccdrid) {
    return sectionRepository.findByCcdrId(ccdrid).getUrl().trim();
  }

  @GetMapping(path="/getcallbyext" , produces = MediaType.APPLICATION_XML_VALUE)
  public List<CallResponse> getcallbyext(@RequestParam(value="extension") String extension,
                                          @RequestParam(required = false,value = "startdate") String startDate,
                                          @RequestParam(required = false,value = "enddate") String endDate ) throws ParseException {
    List<CallResponse> callResponses =new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date sDate = sdf.parse("1900-01-01");
    Date eDate = sdf.parse("2200-01-01");
    if(startDate!=null) {
      sDate=sdf.parse(startDate);

    }
    if(endDate!=null){
      eDate=sdf.parse(endDate);
    }
    List<Section> completedCalls=sectionRepository.findbyExtension(extension,sDate,eDate);
    for(Section sec:completedCalls) {
      CallResponse callResponse = new CallResponse();
      callResponse.setCcdrId(sec.getCcdrId());
      callResponse.setCalledName(sec.getDestinationName());
      callResponse.setCallerName(sec.getSourceName());
      callResponse.setGlobalCallID(sec.getNativeId());
      callResponse.setCallerNumber(sec.getSourceCallerId());
      callResponse.setCalledNumber(sec.getDestinationCallerId());
      callResponses.add(callResponse);
    }

    return callResponses;


  }

}
