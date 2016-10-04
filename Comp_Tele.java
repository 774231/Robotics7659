/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class Comp_Tele extends OpMode {

    /*
     * Note: the configuration of the servos is such that
     * as the arm servo approaches 0, the arm position moves up (away from the floor).
     * Also, as the lever_right servo approaches 0, the lever_right opens up (drops the game element).
     */
    // TETRIX VALUES.
    final static double ARM_MIN_RANGE  = 0.20;
    final static double ARM_MAX_RANGE  = 0.90;
    final static double LEVER_RIGHT_MIN_RANGE  = 0.3;
    final static double LEVER_RIGHT_MAX_RANGE  = 0.95;
    final static double LEVER_LEFT_MIN_RANGE  = 0.25;
    final static double LEVER_LEFT_MAX_RANGE  = 0.9;
    final static double BLADE_MIN_RANGE  = 0;
    final static double BLADE_MAX_RANGE  = 1;

    // position of the arm servo.
    double armPosition;

    // amount to change the arm servo position.
    double armDelta = 0.1;

    // position of the lever_right servo
    double lever_rightPosition;

    // amount to change the lever_right servo position by
    double lever_rightDelta = 0.1;

    double lever_leftPosition;

    double lever_leftDelta = 0.1;

    double blade_Position;

    //double blade_toggle = 1;

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorspool;
    DcMotor motorlift;
    Servo lever_right;
    Servo lever_left;
    Servo arm;
    Servo blade;


    /**
     * Constructor
     */
    public Comp_Tele() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot.
		 *   
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        lever_right = hardwareMap.servo.get("servo_1");
        lever_left = hardwareMap.servo.get("servo_2");
        arm  = hardwareMap.servo.get("servo_3");
        blade  = hardwareMap.servo.get("servo_4");
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorspool = hardwareMap.dcMotor.get("motor_3");
        motorlift = hardwareMap.dcMotor.get("motor_4");
        motorRight.setDirection(DcMotor.Direction.REVERSE);

        // assign the starting position of the wrist and claw
        armPosition = 1;
        lever_rightPosition = 0;
        lever_leftPosition = 1;
        blade_Position = 0;
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {
        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        //float throttle = -gamepad1.left_stick_y;
        //float direction = gamepad1.left_stick_x;
        //float right = throttle - direction;
        //float left = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        //right = Range.clip(right, 0, 1);
        //left = Range.clip(left, 0, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        //right = (float)scaleInput(right);
        //left =  (float)scaleInput(left);

        // write the values to the motors
        //motorRight.setPower(right);
        //motorLeft.setPower(left);

        if (gamepad2.a) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            armPosition += armDelta;
        }
        if (gamepad2.y) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            armPosition -= armDelta;
        }
        //update the position of the lever_right
        if (gamepad2.right_stick_y>0.1) {
            lever_rightPosition += lever_rightDelta;
        }

        if (gamepad2.right_stick_y<-0.1) {
            lever_rightPosition -= lever_rightDelta;
        }
        if (gamepad2.left_stick_y>0.1) {
            lever_leftPosition -= lever_leftDelta;
        }

        if (gamepad2.left_stick_y<-0.1) {
            lever_leftPosition += lever_leftDelta;
        }

        if (gamepad2.right_bumper == true) {
            // if the x button is pushed on gamepad2, increment the position of
            // the arm servo.
            motorspool.setPower(1);
        }
//        else {
//            motorspool.setPower(0);
//        }

        if (gamepad2.left_bumper == true) {
            // if the x button is pushed on gamepad2, increment the position of
            // the arm servo.
            motorspool.setPower(-1);
        }
        if (gamepad2.right_bumper == false) {
            if (gamepad2.left_bumper == false){
                motorspool.setPower(0);
            }
        }

        if (gamepad2.left_bumper == false) {
            if (gamepad2.right_bumper == false){
                motorspool.setPower(0);
            }
        }
//        else {
//            motorspool.setPower(0);
//        }

        if (gamepad2.left_trigger != 0) {
            // if the x button is pushed on gamepad2, increment the position of
            // the arm servo.
            motorlift.setPower(.25);
//            if (gamepad2.left_trigger == 0) {
//                // if the x button is pushed on gamepad2, increment the position of
//                // the arm servo.
//                motorlift.setPower(0);
//            }
        }
//        else {
//            motorlift.setPower(0);
//        }

        if (gamepad2.right_trigger != 0) {
            // if the x button is pushed on gamepad2, increment the position of
            // the arm servo.
            motorlift.setPower(-.25);
//            if (gamepad2.right_trigger == 0) {
//                // if the x button is pushed on gamepad2, increment the position of
//                // the arm servo.
//                motorlift.setPower(0);
//            }
        }

        if (gamepad2.right_trigger == 0) {
            if (gamepad2.left_trigger == 0){
                motorlift.setPower(0);
            }
        }

        if (gamepad2.left_trigger == 0) {
            if (gamepad2.right_trigger == 0){
                motorlift.setPower(0);
            }
        }
//        else {
//            motorlift.setPower(0);
//        }
        if (gamepad2.b) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            blade_Position = BLADE_MAX_RANGE;
        }
        if (gamepad2.x) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            blade_Position = BLADE_MIN_RANGE;
        }

//        if (gamepad2.b) {
//            if (blade_toggle == 0) {
//                blade_Position = BLADE_MAX_RANGE;
//                blade_toggle = 1;
//            }
//            else if (blade_toggle == 1) {
//                blade_Position = BLADE_MIN_RANGE;
//                blade_toggle = 0;
//            }
//             //if the b button is pushed on gamepad2, toggles the blade up or down
//            //blade_toggle = blade_Position;
//            blade_Position = Range.clip(blade_Position, BLADE_MIN_RANGE, BLADE_MAX_RANGE);
//            blade.setPosition(blade_Position);
//            int count = 1;
//            while (count < 5000) {
//                count ++;
//            }
        //}

        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        lever_rightPosition = Range.clip(lever_rightPosition, LEVER_RIGHT_MIN_RANGE, LEVER_RIGHT_MAX_RANGE);
        lever_leftPosition = Range.clip(lever_leftPosition, LEVER_LEFT_MIN_RANGE, LEVER_LEFT_MAX_RANGE);
        blade_Position = Range.clip(blade_Position, BLADE_MIN_RANGE, BLADE_MAX_RANGE);

        //lever_left.setPosition(gamepad2.left_stick_y);
        //lever_right.setPosition(gamepad2.right_stick_y);

        motorLeft.setPower(gamepad1.left_stick_y);
        motorRight.setPower(gamepad1.right_stick_y);


        lever_left.setPosition(lever_leftPosition);
        lever_right.setPosition(lever_rightPosition);
        arm.setPosition(armPosition);
        blade.setPosition(blade_Position);

        //telemetry.addData("Text", "*** Robot Data***");
        //telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", motorLeft));
        //telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", motorRight));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
