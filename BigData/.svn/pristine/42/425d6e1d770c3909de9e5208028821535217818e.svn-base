var width = 860,
    height = 900,
    radius = Math.min(width, height) / 2;

var color = d3.scale.ordinal()
    .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);

var arc = d3.svg.arc()
    .outerRadius(radius - 10)
    .innerRadius(0);

var labelArc = d3.svg.arc()
    .outerRadius(radius - 40)
    .innerRadius(radius - 40);

var pie = d3.layout.pie()
    .sort(null)
    .value(function(d) { return d.Population; });

var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height)
  .append("g") 
    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

d3.csv("../data/bubbledata.csv", type, function(error, data) {
  if (error) throw error;
  
	//Sort the Data based on Region
	data.sort(function(a,b) {return  d3.descending(b.Region,a.Region);});
	
  var g = svg.selectAll(".arc")
      .data(pie(data))
    .enter().append("g")
      .attr("class", "arc");

  g.append("path")
      .attr("d", arc)
      .style("fill", function(d) { return color(d.data.Region); });

  g.append("text")
      .attr("transform", function(d) { return "translate(" + labelArc.centroid(d) + ")"; })
      .attr("dy", ".3em")
      .text(function(d) { return d.data.Country; }).attr("class", "cText");

//Browser Default Tool Tip
  svg.selectAll('.arc').append("title").text(function(d) {  return d.data.Country + "\n" + d.data.GDP});
  
//Custom Tool Tip
  svg.selectAll('.arc').on("mouseover", function(d) {
  		
  	var currentx = d3.transform(d3.select(this).select('text').attr("transform")).translate[0];
  	var currenty = d3.transform(d3.select(this).select('text').attr("transform")).translate[1];
//   			alert(currentx);
  					
  					
  					d3.select("#tooltip").select("#value")
						.html("<p>Country: " + d.data.Country + "</p>" + "<p>Population: " +(d.data.Population/10e5) +"</p>");
  					
  					d3.select(this).select('path').style("fill", "purple");
  			//Show the tooltip
  			d3.select("#tooltip").classed("hidden", false);
  		});

  svg.selectAll('.arc').on("mouseout", function() {
  	//Hide the tooltip
  	d3.select("#tooltip").classed("hidden", true);
  	d3.select(this).select('path').style("fill", function(d) { return color(d.data.Region)});
  })
  
});



function type(d) {
  d.Population = +d.Population;
  return d;
}
