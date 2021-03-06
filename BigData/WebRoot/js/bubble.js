
var width = 1800,    height = 800,    padding = 100;

var svg = d3.select('#graph').append('svg').attr('width', width).attr('height', height)
//move 0,0 slightly down and right to accomodate axes
.attr("transform", "translate(30,20)");


var xScale = d3.scale.linear().domain([0, 40000]).range([padding, width - padding * 2]);
var xAxis = d3.svg.axis().scale(xScale).orient("bottom");
svg.append("g").attr("transform", "translate(0," + (height - padding) + ")").attr("class", "axis").call(xAxis);

var yScale = d3.scale.linear().domain([0,100]).range([height - padding, padding]);
var yAxis = d3.svg.axis().scale(yScale).orient("left");	
svg.append("g").attr("transform", "translate("+padding+",0)").attr("class", "axis").call(yAxis);

//Draw Y-axis grid lines
svg.selectAll("line.y")
.data(yScale.ticks(10))
.enter().append("line")
.attr("class", "y")
.attr("x1", 0)
.attr("x2", 3000)
.attr("y1", yScale)
.attr("y2", yScale)
.style("stroke", "#ccc");

svg.selectAll("line.x")
.data(xScale.ticks(10))
.enter().append("line")
.attr("class", "x")
.attr("x1", xScale)
.attr("x2", xScale)
.attr("y1", 0)
.attr("y2", 800)
.style("stroke", "#ccc");

d3.csv("../data/bubbledata.csv", function(error, rows) {
	if (error)
		throw error;

	//Sord the Data based on population
	rows.sort(function(a,b) {return b.Population-a.Population;});

	var vis = svg.selectAll('circle').data(rows);
	vis.enter().append('circle').attr("r", function(d) {
		var radius = d3.scale.sqrt().domain([0, 1e6]).range([0, 5]);
		return radius(d.Population);
	}).attr('cx', function(d) {
		var xy = d3.scale.linear().domain([0, 40000]).range([200, 7000]);
		var  GDP=xy(d.GDP);
		GDP=GDP||1000000;
		return xy(GDP);
	}).attr('cy', function(d) {
		var lifeScale = d3.scale.linear().domain([0,80 ]).range([0, 200]);
		var life= lifeScale(d.life2015);
		life=life ||60;
		return lifeScale(life);
	}).attr('class', function(d) {
		return d.Region;
	}).append("title").text(function(d) { return d.Country});
	
	//Custom Tool Tip
	svg.selectAll('circle').on("mouseover", function(d) {
				var xPosition = d3.select(this).attr("cx");
			  	var yPosition = d3.select(this).attr("cy");
//			  	alert('helo' + xPosition + " " + yPosition);
				//Update the tooltip position and value
				d3.select("#tooltip").style("left", xPosition + "px")
						.style("top", yPosition + "px").select("#value")
						.text(d.Country);
				
				//Show the tooltip
				d3.select("#tooltip").classed("hidden", false).transition()		
                .duration(200)		
                .style("opacity", .9);	;
			});

	svg.selectAll('circle').on("mouseout", function() {
		//Hide the tooltip
		d3.select("#tooltip").classed("hidden", true);
	})


});


