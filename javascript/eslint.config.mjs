import pluginJs from "@eslint/js";

/** @type {import("eslint").Linter.Config[]} */
export default [
    {
        ignores: ["env/", "build/"]
    },
    {
        files: ["**/*.js"],
        languageOptions: {
            sourceType: "script"
        }
    },
    pluginJs.configs.recommended,
    {
        // See https://eslint.org/docs/latest/rules/
        rules: {
            // Default rules, see https://eslint.org/docs/latest/rules/#possible-problems
            // These rules are overridden. They were deactivated per default.
            "array-callback-return": "warn",
            "no-await-in-loop": "warn",
            "no-constructor-return": "warn",
            "no-duplicate-imports": "error",
            "no-inner-declarations": "warn",
            "no-promise-executor-return": "error",
            "no-self-compare": "error",
            "no-template-curly-in-string": "warn",
            "no-unreachable-loop": "warn",
            "no-use-before-define": [
                "error",
                {
                    functions: false
                }
            ],
            "no-useless-assignment": "warn",
            "require-atomic-updates": "error",

            // Suggested additional rules, see https://eslint.org/docs/latest/rules/#suggestions
            // They were all deactivated per default.
            "block-scoped-var": "error",
            "camelcase": "error",
            "default-case": "error",
            "default-case-last": "error",
            "eqeqeq": "error",
            "max-depth": "warn",
            "max-lines": ["warn", 900],
            "max-nested-callbacks": ["warn", 5],
            "new-cap": "error",
            "no-eq-null": "error",
            "no-implicit-globals": "warn",
            "no-invalid-this": "error",
            "no-lone-blocks": "warn",
            "no-loop-func": "warn",
            "no-nested-ternary": "warn",
            "no-new": "warn",
            "no-new-func": "warn",
            "no-new-wrappers": "warn",
            "no-object-constructor": "warn",
            "no-octal-escape": "warn",
            "no-param-reassign": "error",
            "no-sequences": "warn",
            "no-throw-literal": "warn",
            "no-undef-init": "warn",
            "no-unused-expressions": "warn",
            "no-var": "warn",
            "no-void": "warn",
            "prefer-const": "warn",
            "require-await": "warn"
        }
    }
];
