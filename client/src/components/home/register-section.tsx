import Image from "next/image";
import { LayoutEffect } from "../global/layer-effect";
import { GradientWrapper } from "../global/gradient-wrapper";
import Link from "next/link";
import { ChevronRight } from "lucide-react";

const Register = () => (
  <section>
    <GradientWrapper wrapperClassName="max-w-xs h-[13rem] top-12 inset-0">
      <div className="custom-screen py-28 relative">
        <LayoutEffect
          className="duration-1000 delay-300"
          isInviewState={{
            trueState: "opacity-1",
            falseState: "opacity-0 translate-y-6",
          }}
        >
          <div className="relative z-10">
            <div className="max-w-xl mx-auto text-center">
              <h2 className="text-3xl font-semibold sm:text-4xl">
                Unleash the Power of AI with Email Marketing
              </h2>
              <p className="mt-5 text-muted-foreground">
                Mailgo is the perfect answer! Our AI-based email marketing
                platform enables you to create highly targeted email campaigns
                that are tailored to each individual subscriber.
              </p>
            </div>
            <div className="mt-5 flex justify-center font-medium text-sm">
              <Link
                href="/register"
                className="py-2.5 px-4 text-center rounded-full duration-150 flex items-center text-white bg-blue-600 hover:bg-blue-500 active:bg-blue-700"
              >
                Start now
                <ChevronRight />
              </Link>
            </div>
          </div>
        </LayoutEffect>
        <img
          src="bg-pattern-register-section.webp"
          className="w-full h-full object-cover m-auto absolute inset-0 pointer-events-none"
          alt="Background pattern"
        />
      </div>
    </GradientWrapper>
  </section>
);

export { Register };
