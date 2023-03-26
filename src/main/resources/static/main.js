let indexOfMeasuredTemperatures = data.indexOfMeasuredTemperatures
let temperatures = data.temperatures

let graph = document.getElementsByTagName('canvas')[0]

let drawLine = (ctx, begin, end, stroke = 'black', width = 1) => {
    ctx.save()
    if (stroke) {
        ctx.strokeStyle = stroke;
    }
    if (width) {
        ctx.lineWidth = width;
    }
    ctx.beginPath();
    ctx.moveTo(...begin);
    ctx.lineTo(...end);
    ctx.stroke();
    ctx.restore()
}

window.ctx = graph.getContext('2d')
let ratio = window.innerHeight / window.innerWidth
graph.height = window.innerHeight
graph.width = window.innerWidth
let width = graph.width
let height = graph.height

ctx.fillStyle = 'lightgray'
ctx.fillRect(0, 0, width, height)
let fontSize = Math.round(height / 40)
ctx.font = fontSize + "px 'Inconsolata'"
ctx.fontStretch = "condensed";
ctx.textAlign = "end"

ctx.transform(0.9, 0, 0, -0.8, width * 0.07, height * 0.83)

drawLine(ctx, [0, 0], [width, 0])
drawLine(ctx, [0, 0], [0, height])

let drawText = (text, x, y, align, color = "gray") => {
    ctx.save()
    ctx.scale(1, -1)
    ctx.fillStyle = color
    ctx.textAlign = align
    ctx.fillText(text, x, y)
    ctx.restore()
}

let xTicks = () => {
    let xTicks = temperatures.length - 1
    for (let i = 0; i <= xTicks; i++) {
        let x = width * i / xTicks
        if (i % 144 == 0) {
            drawLine(ctx, [x, -10], [x, height], 'black')
        } else if (i % 36 == 0) {
            drawLine(ctx, [x, -10], [x, height], 'gray')
        }
        if (i % 36 == 0) {
            drawText((temperatures[i]["time"]).replace(':00', '').replace('0', ''), x, fontSize * 1.8, "center")
        }
        if (i % 144 == 72) {
            drawText((temperatures[i]["day"]), x, 2.8 * fontSize, "center")
            drawText((temperatures[i]["date"]), x, 3.8 * fontSize, "center")
        }
    }
}

let yTickMin = 0
let yTickMax = 20
let yTicks
let offset = 0.5 - Number.EPSILON
yTicks = () => {
    let yTickMinO = Math.round(-offset + Math.min(...temperatures.map(x => x["outdoorTemp"]).filter(x => x)))
    let yTickMaxO = Math.round(offset + Math.max(...temperatures.map(x => x["outdoorTemp"])))
    let yTickMinR = Math.round(-offset + Math.min(...temperatures.map(x => x["roomTemp"]).filter(x => x)))
    let yTickMaxR = Math.round(offset + Math.max(...temperatures.map(x => x["roomTemp"])))
    let color
    yTickMin = yTickMinO < yTickMinR ? yTickMinO : yTickMinR
    yTickMax = yTickMaxO > yTickMaxR ? yTickMaxO : yTickMaxR
    /* yTickMin = yTickMinR
        yTickMax = yTickMaxR */
    yTicks = yTickMax - yTickMin
    for (let i = 0; i <= yTicks; i++) {
        let y = height * i / yTicks
        color = (i + yTickMin) == 0 ? "black" : "gray"
        drawLine(ctx, [-10, y], [width, y], color)
        drawText(i + yTickMin, -fontSize, -y + fontSize / 3)
    }
}

let graphIndoor = () => {
    let previousX;
    let previousY;
    let x;
    let y;
    let colorOn = false;
    let color
    color = "rgba(0, 0, 0, 0)";
    for (let i = 0; i < temperatures.length; i++) {
        x = i * width / temperatures.length;
        y = (height / yTicks) * (temperatures[i]["roomTemp"] - yTickMin);
        if (colorOn) {
            color = i <= indexOfMeasuredTemperatures ? "blue" : "red"
        }
        drawLine(ctx, [previousX, previousY], [x, y], color, 4)
        if (temperatures[i]["roomTemp"] != null) {
            colorOn = true;
        }
        previousX = x;
        previousY = y;
    }
}

let graphOutdoor = () => {
    let color;
    let colorSwitch = true
    let previousX = 0
    let previousY = (height / yTicks) * (temperatures[0]["outdoorTemp"] - yTickMin);
    let x
    let y
    for (let i = 0; i < temperatures.length; i++) {
        x = i * width / temperatures.length
        y = (height / yTicks) * (temperatures[i]["outdoorTemp"] - yTickMin);
        colorSwitch = true
        if (temperatures[i]["outdoorTemp"] == null) {
            color = "rgba(0, 0, 0, 0)"
            colorSwitch = false
        }
        drawLine(ctx, [previousX, previousY], [x, y], color, 3)
        if (colorSwitch) {
            color = "navy"
        }
        previousX = x;
        previousY = y;
    }
}

xTicks()
yTicks()
graphOutdoor()
graphIndoor()
