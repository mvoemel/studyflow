import Link from "next/link";
import { LayoutEffect } from "../global/layer-effect";
import { GradientWrapper } from "../global/gradient-wrapper";
import { ChevronRight } from "lucide-react";

const Hero = () => (
  <section>
    <div className="custom-screen py-28">
      <LayoutEffect
        className="duration-1000 delay-300"
        isInviewState={{
          trueState: "opacity-1",
          falseState: "opacity-0",
        }}
      >
        <div>
          <div className="space-y-5 max-w-3xl mx-auto text-center">
            <h1 className="text-4xl bg-clip-text text-transparent bg-gradient-to-b from-primary to-blue-300 font-extrabold mx-auto sm:text-6xl">
              Conquer Your Semester with Confidence – Join Thousands of
              Successful Students.
            </h1>
            <p className="max-w-xl mx-auto text-gray-300">
              Your Semester, Your Plan: Smarter Studyplans Tailored for You!
            </p>
            <div className="flex justify-center font-medium text-sm">
              <Link
                href="/#features"
                className={`py-2.5 px-4 text-center rounded-full duration-150 flex items-center text-white bg-blue-600 hover:bg-blue-500 active:bg-blue-700`}
              >
                Learn More
                <ChevronRight />
              </Link>
            </div>
          </div>
          <GradientWrapper
            className="mt-16 sm:mt-28"
            wrapperClassName="max-w-3xl h-[250px] top-12 inset-0 sm:h-[300px] lg:h-[650px]"
          >
            <img
              src="/hero.png"
              className="shadow-lg rounded-2xl"
              alt="Mailgo"
            />
          </GradientWrapper>
        </div>
      </LayoutEffect>
    </div>
  </section>
);

export { Hero };
