export default class Graphs {

    constructor() {
        this.xTicks()
        this.yTicks()
        this.graphOutdoor()
        this.graphIndoor()
    }

    indexOfMeasuredTemperatures = data.indexOfMeasuredTemperatures
    temperatures = data.temperatures
    colorIn = data.colorIn
    yTicks
    yTickMin
    yTickMax

    drawLine = (ctx, begin, end, stroke = 'black', width = 1) => {
        ctx.save()
        if (stroke) {
            ctx.strokeStyle = stroke
        }
        if (width) {
            ctx.lineWidth = width
        }
        ctx.beginPath()
        ctx.moveTo(...begin)
        ctx.lineTo(...end)
        ctx.stroke();
        ctx.restore()
    }

    drawText = (text, x, y, align, color = "gray") => {
        ctx.save()
        ctx.scale(1, -1)
        ctx.fillStyle = color
        ctx.textAlign = align
        ctx.fillText(text, x, y)
        ctx.restore()
    }

    xTicks = () => {
        this.drawLine(ctx, [0, 0], [width, 0])
        let xTicks = this.temperatures.length - 1
        for (let i = 0; i <= xTicks; i++) {
            let x = width * i / xTicks
            if (i % 144 == 0) {
                this.drawLine(ctx, [x, -10], [x, height], 'black')
            } else if (i % 36 == 0) {
                this.drawLine(ctx, [x, -10], [x, height], 'gray')
            }
            if (i % 36 == 0) {
                this.drawText((this.temperatures[i]["time"]).replace(':00', '').replace('0', ''), x, fontSize * 1.8, "center")
            }
            if (i % 144 == 72) {
                this.drawText((this.temperatures[i]["day"]), x, 2.8 * fontSize, "center")
                this.drawText((this.temperatures[i]["date"]), x, 3.8 * fontSize, "center")
            }
        }
    }

    yTicks = () => {
        this.drawLine(ctx, [0, 0], [0, height])

        let offset = 0.5 - Number.EPSILON
        let yTickMinO = Math.round(-offset + Math.min(...this.temperatures.map(x => x["outdoorTemp"]).filter(x => x)))
        let yTickMaxO = Math.round(offset + Math.max(...this.temperatures.map(x => x["outdoorTemp"])))
        let yTickMinR = Math.round(-offset + Math.min(...this.temperatures.map(x => x["roomTemp"]).filter(x => x)))
        let yTickMaxR = Math.round(offset + Math.max(...this.temperatures.map(x => x["roomTemp"])))
        let color
        this.yTickMin = yTickMinO < yTickMinR ? yTickMinO : yTickMinR
        this.yTickMax = yTickMaxO > yTickMaxR ? yTickMaxO : yTickMaxR
        /* yTickMin = yTickMinR
            yTickMax = yTickMaxR */
        this.yTicks = this.yTickMax - this.yTickMin
        for (let i = 0; i <= this.yTicks; i++) {
            let y = height * i / this.yTicks
            color = (i + this.yTickMin) == 0 ? "black" : "gray"
            this.drawLine(ctx, [-10, y], [width, y], color)
            this.drawText(i + this.yTickMin, -fontSize, -y + fontSize / 3)
        }
    }

    graphIndoor = () => {
        let previousX
        let previousY
        let x
        let y
        let colorOn = false
        let color
        color = "rgba(0, 0, 0, 0)"
        for (let i = 0; i < this.temperatures.length; i++) {
            x = i * width / this.temperatures.length
            y = (height / this.yTicks) * (this.temperatures[i]["roomTemp"] - this.yTickMin)
            if (colorOn) {
                color = i <= this.indexOfMeasuredTemperatures ? this.colorIn : "red"
            }
            this.drawLine(ctx, [previousX, previousY], [x, y], color, 4)
            if (this.temperatures[i]["roomTemp"] != null) {
                colorOn = true
            }
            previousX = x
            previousY = y
        }
    }

    graphOutdoor = () => {
        let color
        let colorSwitch = true
        let previousX = 0
        let previousY = (height / this.yTicks) * (this.temperatures[0]["outdoorTemp"] - this.yTickMin)
        let x
        let y
        for (let i = 0; i < this.temperatures.length; i++) {
            x = i * width / this.temperatures.length
            y = (height / this.yTicks) * (this.temperatures[i]["outdoorTemp"] - this.yTickMin)
            colorSwitch = true
            if (this.temperatures[i]["outdoorTemp"] == null) {
                color = "rgba(0, 0, 0, 0)"
                colorSwitch = false
            }
            this.drawLine(ctx, [previousX, previousY], [x, y], color, 3)
            if (colorSwitch) {
                color = "navy"
            }
            previousX = x
            previousY = y
        }
    }
}