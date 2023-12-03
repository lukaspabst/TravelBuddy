export const validateLink = (platform, link) => {
    const platformRegexMap = {
        'Facebook': /^(https?:\/\/)?(www\.)?facebook\.com\/(?:[a-zA-Z0-9._]+\/?)?$/,
        'Twitter': /^(https?:\/\/)?(www\.)?twitter\.com\/(?:[a-zA-Z0-9_]+\/?)?$/,
        'Instagram': /^(https?:\/\/)?(www\.)?instagram\.com\/(?:[a-zA-Z0-9._]+\/?)?$/,
        'LinkedIn': /^(https?:\/\/)?(www\.)?linkedin\.com\/(?:in\/[a-zA-Z0-9._]+\/?)?$/,
        'Xing': /^(https?:\/\/)?(www\.)?xing\.com\/profile\/(?:[a-zA-Z0-9._]+\/?)?$/,
        'YouTube': /^(https?:\/\/)?(www\.)?youtube\.com\/(?:channel\/[a-zA-Z0-9._]+\/?)?$/,
        'Twitch': /^(https?:\/\/)?(www\.)?twitch\.tv\/(?:[a-zA-Z0-9._]+\/?)?$/,
        'Snapchat': /^(https?:\/\/)?(www\.)?snapchat\.com\/add\/(?:[a-zA-Z0-9._]+\/?)?$/,
        'Pinterest': /^(https?:\/\/)?(www\.)?pinterest\.com\/(?:[a-zA-Z0-9._]+\/?)?$/,
        'Reddit': /^(https?:\/\/)?(www\.)?reddit\.com\/user\/(?:[a-zA-Z0-9._]+\/?)?$/,
        'Tumblr': /^(https?:\/\/)?(www\.)?tumblr\.com\/(?:[a-zA-Z0-9._]+\/?)?$/,
        'GitHub': /^(https?:\/\/)?(www\.)?github\.com\/(?:[a-zA-Z0-9._-]+\/?)?$/,
        'GitLab': /^(https?:\/\/)?(www\.)?gitlab\.com\/(?:[a-zA-Z0-9._-]+\/?)?$/,
        'Bitbucket': /^(https?:\/\/)?(www\.)?bitbucket\.org\/(?:[a-zA-Z0-9._-]+\/?)?$/,
    };

    const regex = platformRegexMap[platform];
    return regex ? regex.test(link) : true;
};
