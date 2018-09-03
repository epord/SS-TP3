var file;
function preload() {
  file = loadStrings('output.txt');
}


var worldSize;
var canvasSize;
var particles;
var particles_count;
var frames_count = 4999;
var time_checkpoint;
var minSpeed =  null;
var maxSpeed = null

function setup() {
  // put setup code here
  worldSize = {height: parseFloat(file[0].split(" ")[1]), width: parseFloat(file[0].split(" ")[0])};
  if (worldSize.height > worldSize.width) {
    canvasSize = {height: 600, width: 600 * worldSize.width / worldSize.height};
  } else {
      canvasSize = {height: 600 * worldSize.height / worldSize.width, width: 600};
  }
  particles_count = parseInt(file[0].split(" ")[2]);
  particles = new Array(frames_count);
  createCanvas(canvasSize.width, canvasSize.height);
    // createCanvas(800, 800);

  for (var i = 0; i*particles_count < file.length-1; i++) {
    particles[i] = new Array();
      for (var j = 0; j < particles_count; j++) {
          var x = parseFloat(file[1+i*particles_count+j].split(",")[1]);
          var y = parseFloat(file[1+i*particles_count+j].split(",")[2]);
          var velocityX = parseFloat(file[1+i*particles_count+j].split(",")[3]);
          var velocityY = parseFloat(file[1+i*particles_count+j].split(",")[4]);
          var speed = sqrt(velocityX*velocityX + velocityY*velocityY);
          var r = parseFloat(file[1+i*particles_count+j].split(",")[5]);
          particles[i].push({x: x, y: y, radius: r, speed: speed})
          minSpeed = (!minSpeed || minSpeed > speed) ? speed : minSpeed;
          maxSpeed = (!maxSpeed || maxSpeed < speed) ? speed : maxSpeed;
      }
  }
    console.log(minSpeed)
    console.log(maxSpeed)
  console.log("setup finished!")
  time_checkpoint = millis();
}

var frame = 0;
var time_between_frames = 0;
var frame_skip = 5;
function draw() {
  // put drawing code here
  background(200);

  for (var j = 0; j < particles_count; j++) {
      drawParticle(particles[frame][j].x, particles[frame][j].y, particles[frame][j].radius, particles[frame][j].speed)
  }

  if (millis() - time_checkpoint > time_between_frames) {
      frame = (frame + frame_skip) % (parseInt(frames_count));
      time_checkpoint = millis();``
      // console.log(frame)
  }
}

function drawParticle(x, y, radius, speed) {
  var redIntensity = (speed - minSpeed) / (maxSpeed - minSpeed) * 255;
  fill(255, 255-redIntensity, 255-redIntensity);
  ellipse(x * (canvasSize.width / worldSize.width), y * (canvasSize.height / worldSize.height),
      radius * 2 * (canvasSize.width / worldSize.width), radius * 2 * (canvasSize.height / worldSize.height));
}