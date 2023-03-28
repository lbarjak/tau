export default class GraphFrame {

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
        let xTicks = temperatures.length - 1
        for (let i = 0; i <= xTicks; i++) {
            let x = width * i / xTicks
            if (i % 144 == 0) {
                this.drawLine(ctx, [x, -10], [x, height], 'black')
            } else if (i % 36 == 0) {
                this.drawLine(ctx, [x, -10], [x, height], 'gray')
            }
            if (i % 36 == 0) {
                this.drawText((temperatures[i]["time"]).replace(':00', '').replace('0', ''), x, fontSize * 1.8, "center")
            }
            if (i % 144 == 72) {
                this.drawText((temperatures[i]["day"]), x, 2.8 * fontSize, "center")
                this.drawText((temperatures[i]["date"]), x, 3.8 * fontSize, "center")
            }
        }
    }

    yTicks = () => {
        this.drawLine(ctx, [0, 0], [0, height])
        let yTickMin = 0
        let yTickMax = 20
        let yTicks
        let offset = 0.5 - Number.EPSILON
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
            this.drawLine(ctx, [-10, y], [width, y], color)
            this.drawText(i + yTickMin, -fontSize, -y + fontSize / 3)
        }
    }

}