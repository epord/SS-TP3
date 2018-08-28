var file;
function preload() {
  file = loadStrings('random.txt');
}


var worldSize;
var canvasSize;
var particles = [];
var particles_count;

function setup() {
  // put setup code here
  worldSize = {height: file[0].split(" ")[0], width: file[0].split(" ")[1]};
  if (worldSize.height > worldSize.width) {
    canvasSize = {height: 600, width: 600 * worldSize.width / worldSize.height};
  } else {
      canvasSize = {height: 600 * worldSize.height / worldSize.width, width: 600};
  }
  createCanvas(canvasSize.width, canvasSize.height);

  for (var i = 1; i < file.length; i++) {
    var x = parseFloat(file[i].split(" ")[0]);
    var y = parseFloat(file[i].split(" ")[1]);
    var r = parseFloat(file[i].split(" ")[2]);
    particles.push({x: x, y: y, radius: r})
  }
  // console.log(file)
  console.log("setup finished!")
}

function draw() {
  // put drawing code here
  background(200);

  for (var i = 0; i < particles.length ; i++) {
    drawParticle(particles[i].x, particles[i].y, particles[i].radius, "red")
  };
}

function drawParticle(x, y, radius, color) {
  fill(color);
  ellipse(x * (canvasSize.width / worldSize.width), y * (canvasSize.height / worldSize.height), radius * (canvasSize.width / worldSize.width), radius * (canvasSize.height / worldSize.height));
}